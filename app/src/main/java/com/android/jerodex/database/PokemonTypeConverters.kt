package com.android.jerodex.database

import androidx.room.TypeConverter
import com.android.jerodex.api.Sprites
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class SpritesTypeConverter {
    private val moshi = Moshi.Builder().build()
    private val adapter: JsonAdapter<Sprites> = moshi.adapter(Sprites::class.java)

    @TypeConverter
    fun fromSprites(sprites: Sprites): String {
        return adapter.toJson(sprites)
    }

    @TypeConverter
    fun toSprites(spritesJson: String): Sprites? {
        return adapter.fromJson(spritesJson)
    }
}

class StringListTypeConverter {

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    fun toString(list: List<String>?): String? {
        return list?.joinToString(",") { it }
    }
}