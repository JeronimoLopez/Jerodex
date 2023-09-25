package com.android.jerodex.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Types(
    @Json(name = "types") val types: List<Type>
)