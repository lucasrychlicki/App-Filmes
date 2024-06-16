package com.lucaslima.projetonetflixapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lucaslima.projetonetflixapi.api.RetrofitService
import com.lucaslima.projetonetflixapi.databinding.ItemFilmeBinding
import com.lucaslima.projetonetflixapi.model.Filme
import com.squareup.picasso.Picasso

class FilmeAdapter(
    val onClick: (Filme) -> Unit
) : RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    private var listaFilmes = mutableListOf<Filme>()

    fun adicionarLista(lista: List<Filme>) {
        this.listaFilmes.addAll(lista)
        notifyDataSetChanged()
    }

    inner class FilmeViewHolder(val binding: ItemFilmeBinding)
        : RecyclerView.ViewHolder(binding.root) {

            fun bind(filme: Filme) {
                val nomeFilme = filme.backdrop_path
                val tamanhoFilme = "w780" //w300, w780, w1280
                val urlBase = RetrofitService.BASE_URL_IMAGEM

                val urlFilme = urlBase + tamanhoFilme + nomeFilme

                Picasso.get()
                    .load(urlFilme)
                    .into(binding.imgItemFilme)

                binding.textTitulo.text = filme.title
                binding.clItem.setOnClickListener {
                    onClick (filme)
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFilmeBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return FilmeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        val filme = listaFilmes[position]
        holder.bind(filme)
    }

    override fun getItemCount(): Int {
        return listaFilmes.size
    }


}