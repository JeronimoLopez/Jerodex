package com.android.jerodex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PokedexDao {
    @Query("SELECT * FROM PokemonInformation")

    fun getPokemonList(): Flow<List<PokemonInformation>>
    @Query("SELECT * FROM PokemonInformation WHERE name=(:name)")
    suspend fun getPokemon(name:String):PokemonInformation?

    @Query("SELECT * FROM PokemonInformation WHERE id = :id")
    suspend fun getPokemonId(id: Int): PokemonInformation?
    @Update
    suspend fun updatePokemon(pokemonInformation: PokemonInformation)

    @Insert
    suspend fun addPokedex(pokemonInformation: PokemonInformation)
}