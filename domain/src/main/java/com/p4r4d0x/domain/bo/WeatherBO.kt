package com.p4r4d0x.domain.bo

import com.squareup.moshi.Json

data class WeatherBO(
    @Json(name = "humidity")
    val humidity: Int,
    @Json(name = "temperature")
    val temperature: Int
)