package com.lucaslima.projetonetflixapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.lucaslima.projetonetflixapi.adapter.FilmeAdapter
import com.lucaslima.projetonetflixapi.api.RetrofitService
import com.lucaslima.projetonetflixapi.databinding.ActivityMainBinding
import com.lucaslima.projetonetflixapi.model.Filme
import com.lucaslima.projetonetflixapi.model.FilmeRecente
import com.lucaslima.projetonetflixapi.model.FilmeResposta
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var paginaAtual = 1
    private val TAG = "info_filme"
    private val binding by lazy {
        ActivityMainBinding.inflate( layoutInflater )
    }

    private val filmeAPI by lazy {
        RetrofitService.filmeAPI
    }
    var jobFilmeRecente: Job? = null
    var jobFilmesPopulares: Job? = null
    var gridLayoutManager: GridLayoutManager? = null
    private lateinit var filmeAdapter: FilmeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

        inicializarViews()


    }

    private fun inicializarViews() {

        filmeAdapter = FilmeAdapter{filme ->
            val intent = Intent(this, DetalhesActivity::class.java)
            intent.putExtra("filme", filme)
            startActivity(intent)
        }
        binding.rvPopulares.adapter = filmeAdapter

        gridLayoutManager = GridLayoutManager(
            this,
            2,
        )

        /*gridLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )*/
       binding.rvPopulares.layoutManager = gridLayoutManager

        binding.rvPopulares.addOnScrollListener( object : OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val podeDescerVerticalmente = recyclerView.canScrollVertically(1)
                if (!podeDescerVerticalmente){
                    recuperarFilmesPopularesProximaPagina()
                }else{

                }

                /*val ultimoItemVisivel = linearLayoutManager?.findFirstVisibleItemPosition()
                val totalItens= recyclerView.adapter?.itemCount
                //Log.i("recycler_test", "ultimo: $ultimoItemVisivel total: $totalItens")
                if( ultimoItemVisivel != null && totalItens != null){
                    if(totalItens-2 == ultimoItemVisivel ){
                        binding.fabAdicionar.hide()
                    }else{
                        binding.fabAdicionar.show()
                    }
                }*/
                /*Log.i("recycler_test", "onScrolled: dx: $dx dy: $dy")
                if(dy > 0){
                    binding.fabAdicionar.hide()
                }else{
                    binding.fabAdicionar.show()
                }*/
            }

        })
    }


    override fun onStart() {
        super.onStart()
        recuperarFilmeRecente()
        recuperarFilmesPopulares()
    }

    private fun recuperarFilmeRecente() {
        jobFilmeRecente = CoroutineScope(Dispatchers.IO).launch {
            var resposta: Response<FilmeRecente>? = null

            try {
                resposta = filmeAPI.recuperarFilmeRecente()
            }catch (e: Exception){
                exibirMensagem("Erro ao fazer a requisição")
            }

            if(resposta != null){
                if(resposta.isSuccessful){
                    val filmeRecente = resposta.body()
                    val nomeImagem = filmeRecente?.poster_path
                    val tituloFilme = filmeRecente?.title
                    val url = RetrofitService.BASE_URL_IMAGEM + "w780" + nomeImagem

                    withContext( Dispatchers.Main ){
                        /*val texto = "titulo: $tituloFilme url: $url"
                        binding.textPopulares.text = texto*/
                        Picasso.get()
                            .load(url)
                            .error( R.drawable.capa )
                            .into(binding.imgCapa)
                    }
                }else{
                    exibirMensagem("Não foi possível recuperar o filme recente CODIGO: ${resposta.code()}")
                }
            }else{
                exibirMensagem("Não foi possível fazer a requisição")
            }
        }
    }

    private fun recuperarFilmesPopularesProximaPagina(){
        if(paginaAtual < 38029){
            paginaAtual++
            recuperarFilmesPopulares(paginaAtual)
        }
    }

    private fun recuperarFilmesPopulares(pagina: Int = 1) {
        jobFilmesPopulares = CoroutineScope(Dispatchers.IO).launch {
            var resposta: Response<FilmeResposta>? = null

            try {
                resposta = filmeAPI.recuperarFilmesPopulares(pagina)
            }catch (e: Exception){
                exibirMensagem("Erro ao fazer a requisição")
            }

            if(resposta != null){
                if(resposta.isSuccessful){
                    val filmeResposta = resposta.body()
                    val listaFilmes = filmeResposta?.filmes
                    if( listaFilmes != null && listaFilmes.isNotEmpty()){

                        withContext(Dispatchers.Main){
                            filmeAdapter.adicionarLista(listaFilmes)
                        }

                        /*Log.i("filmes_api", "lista Filmes: ")
                        listaFilmes.forEach{filme ->
                            Log.i("filmes_api", "Titulo: ${filme.title}")
                        }*/
                    }

                }else{
                    exibirMensagem("Não foi possível recuperar o filme recente CODIGO: ${resposta.code()}")
                }
            }else{
                exibirMensagem("Não foi possível fazer a requisição")
            }
        }
    }

    private fun exibirMensagem(mensagem:String) {
        Toast.makeText(applicationContext,
            mensagem,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onStop() {
        super.onStop()
        jobFilmeRecente?.cancel()
        jobFilmesPopulares?.cancel()
    }


}