package com.p4r4d0x.data.dto.stats

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StatsWeatherDto(
    val temperature: StatsTemperature = StatsTemperature(),
    val humidity: StatsHumidity = StatsHumidity()
) {
    @JsonClass(generateAdapter = true)
    data class StatsTemperature(
        val isPossible: Boolean = false,
        val level: Int = -1
    )

    @JsonClass(generateAdapter = true)
    data class StatsHumidity(
        val isPossible: Boolean = false,
        val level: Int = -1
    )

}

