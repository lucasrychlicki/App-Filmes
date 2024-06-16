package com.lucaslima.projetonetflixapi.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    companion object {

        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_URL_IMAGEM = "https://image.tmdb.org/t/p/"
        const val API_KEY = "625a3834843a148f3468091557b7165d"

        val retrofit = Retrofit.Builder()
            .baseUrl( BASE_URL )
            .addConverterFactory( GsonConverterFactory.create() )
            .build()

        val filmeAPI = retrofit.create( FilmeAPI::class.java )
    }
}