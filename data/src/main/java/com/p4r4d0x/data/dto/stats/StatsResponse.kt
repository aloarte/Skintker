package com.p4r4d0x.data.dto.stats

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StatsResponse(val type: String, val stats: StatsDto?)