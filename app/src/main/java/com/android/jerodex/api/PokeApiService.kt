package com.android.jerodex.api

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PokeApiService {

    @GET("pokemon-form")
    suspend fun getPokedexList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonListResponse


    @GET
    suspend fun getPokemonByUrl(@Url url: String): PokemonResponse
}