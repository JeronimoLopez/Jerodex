package com.android.jerodex.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Type(
    @Json(name = "slot") val slot:Int,
    @Json(name = "type") val type: TypeName
)