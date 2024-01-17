package com.p4r4d0x.data.dto.stats

data class StatsWeatherDto(
    val temperature: StatsTemperature = StatsTemperature(),
    val humidity: StatsHumidity = StatsHumidity()
) {
    data class StatsTemperature(
        val isPossible: Boolean = false,
        val level: Int = -1
    )

    data class StatsHumidity(
        val isPossible: Boolean = false,
        val level: Int = -1
    )

}

