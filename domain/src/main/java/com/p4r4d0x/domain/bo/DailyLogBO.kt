package com.p4r4d0x.domain.bo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class DailyLogBO(
    @Json(name = "date")
    val date: Date,
    @Json(name = "irritation")
    val irritation: IrritationBO,
    @Json(name = "additionalData")
    val additionalData: AdditionalDataBO,
    @Json(name = "foodList")
    val foodList: List<String>
)