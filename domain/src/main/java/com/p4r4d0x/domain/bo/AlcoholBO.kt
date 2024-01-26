package com.p4r4d0x.domain.bo

import com.squareup.moshi.Json

data class AlcoholBO(
    @Json(name = "level")
    val level: AlcoholLevel,
    @Json(name = "beers")
    val beers: List<String> = emptyList(),
    @Json(name = "wines")
    val wines: List<String> = emptyList(),
    @Json(name = "distilledDrinks")
    val distilledDrinks: List<String> = emptyList()
)
