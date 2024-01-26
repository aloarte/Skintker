package com.p4r4d0x.data.dto.stats

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StatsTravelDto(
    val isPossible: Boolean = false,
    val city: String? = null
)

