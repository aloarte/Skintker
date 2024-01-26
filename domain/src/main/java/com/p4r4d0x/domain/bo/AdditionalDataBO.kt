package com.p4r4d0x.domain.bo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AdditionalDataBO(
    @Json(name = "stressLevel")
    val stressLevel: Int,
    @Json(name = "weather")
    val weather: WeatherBO,
    @Json(name = "travel")
    val travel: TravelBO,
    @Json(name = "alcohol")
    val alcohol: AlcoholBO
)