package com.lucaslima.projetonetflixapi.api

import com.lucaslima.projetonetflixapi.model.Filme
import com.lucaslima.projetonetflixapi.model.FilmeRecente
import com.lucaslima.projetonetflixapi.model.FilmeResposta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmeAPI {

    @GET("movie/latest?api_key=${RetrofitService.API_KEY}")
    suspend fun recuperarFilmeRecente() : Response<FilmeRecente>

    @GET("movie/popular?api_key=${RetrofitService.API_KEY}")
    suspend fun recuperarFilmesPopulares(
        @Query("page") pagina: Int
    ) : Response<FilmeResposta>

}