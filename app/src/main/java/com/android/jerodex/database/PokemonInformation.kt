package com.android.jerodex.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android.jerodex.api.Sprites

@Entity
@TypeConverters(SpritesTypeConverter::class, StringListTypeConverter::class)
data class PokemonInformation(
    @PrimaryKey val name: String,
    val id: Int,
    val sprites: String,
    val types: List<String>,
    val url: String
)
