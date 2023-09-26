package com.android.jerodex

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.android.jerodex.database.PokedexDatabase
import com.android.jerodex.database.PokemonInformation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

private const val TAG = "PokemonRepository"
private const val DATABASE_NAME = "pokedex-database"

class PokemonRepository private constructor(context: Context) {

    private val database: PokedexDatabase = Room.databaseBuilder(
            context.applicationContext, PokedexDatabase::class.java, DATABASE_NAME
        ).build()

    fun getPokemonList(): Flow<List<PokemonInformation>> = database.pokedexDao().getPokemonList()


    suspend fun addPokedex(pokemonInformation: PokemonInformation) {
        val checkPokemon:String? = database.pokedexDao().getPokemon(pokemonInformation.name)?.name
        //checkPokemon is the value in the database
        if(checkPokemon != pokemonInformation.name && checkPokemon.isNullOrEmpty())
        {
            Log.d(TAG, "Added ${pokemonInformation.name} to the Pokedex")
            database.pokedexDao().addPokedex(pokemonInformation)
        } else{
            Log.d(TAG, "Pokemon already exists in database")
        }
    }

    suspend fun getPokemon(name:String):PokemonInformation? {
        return  database.pokedexDao().getPokemon(name)
    }

    suspend fun getPokemonId(id: Int): PokemonInformation?{
        return database.pokedexDao().getPokemonId(id)
    }

    suspend fun updatePokemon(pokemonInformation: PokemonInformation) = database.pokedexDao().updatePokemon(pokemonInformation)



    companion object {
        private var INSTANCE: PokemonRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = PokemonRepository(context)
            }
        }

        fun get(): PokemonRepository {
            return INSTANCE ?: throw IllegalStateException("PokemonRepository must be initialized")
        }
    }
}