package com.android.jerodex

import com.android.jerodex.api.PokeApiService
import com.android.jerodex.api.PokemonListItem
import com.android.jerodex.api.PokemonResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class PokedexRepository {

    private val pokeApiService:PokeApiService
    init {
        val interceptor =  HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
         pokeApiService = retrofit.create(PokeApiService::class.java)
    }

    suspend fun getPokedexList(offset: Int, limit: Int) =
        pokeApiService.getPokedexList(offset, limit)
    suspend fun getPokemonByUrl(url: String) = pokeApiService.getPokemonByUrl(url)
}
