package com.p4r4d0x.domain.bo

import com.squareup.moshi.Json

data class TravelBO(
    @Json(name = "traveled")
    val traveled: Boolean,
    @Json(name = "city")
    val city: String
)