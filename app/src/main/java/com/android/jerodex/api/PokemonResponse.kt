package com.android.jerodex.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class PokemonResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name:String,
    @Json(name = "sprites") val sprites: Sprites,
    @Json(name = "types") val types: List<Type>
)
