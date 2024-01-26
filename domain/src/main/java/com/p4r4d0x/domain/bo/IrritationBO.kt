package com.p4r4d0x.domain.bo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IrritationBO(
    @Json(name = "overallValue")
    val overallValue: Int,
    @Json(name = "zoneValues")
    val zoneValues: List<String>
)