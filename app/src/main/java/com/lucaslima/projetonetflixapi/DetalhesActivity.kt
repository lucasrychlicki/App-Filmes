package com.lucaslima.projetonetflixapi

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.lucaslima.projetonetflixapi.api.RetrofitService
import com.lucaslima.projetonetflixapi.databinding.ActivityDetalhesBinding
import com.lucaslima.projetonetflixapi.model.Filme
import com.squareup.picasso.Picasso

class DetalhesActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetalhesBinding.inflate( layoutInflater )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

        val bundle = intent.extras
        if (bundle != null){
            val filme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("filme", Filme::class.java)
            } else {
                bundle.getParcelable("filme") as? Filme?
            }

            if(filme != null) {
                binding.textFilmeTitulo.text = filme.title
                val nomeFilme = filme.backdrop_path
                val tamanhoFilme = "w780"
                val urlBase = RetrofitService.BASE_URL_IMAGEM

                val urlFilme = urlBase + tamanhoFilme + nomeFilme

                Picasso.get()
                    .load(urlFilme)
                    .into(binding.imgPoster)
                }
            }
        }

    }
