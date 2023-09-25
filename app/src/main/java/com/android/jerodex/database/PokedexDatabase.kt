package com.android.jerodex.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [PokemonInformation::class], version = 1, exportSchema = true)
@TypeConverters(SpritesTypeConverter::class, StringListTypeConverter::class)
abstract class PokedexDatabase : RoomDatabase() {
    abstract fun pokedexDao(): PokedexDao
}