package com.android.jerodex.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonListResponse(
    @Json(name = "count") val count: Int,
    @Json(name = "results") val results: List<PokemonListItem>
)