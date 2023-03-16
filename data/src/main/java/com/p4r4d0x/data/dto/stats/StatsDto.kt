package com.p4r4d0x.data.dto.stats

import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.utils.Constants.DEFAULT_MIN_LOGS

data class StatsDto(
    val relevantLogs: Int,
    val alcoholStats: AlcoholStatsDto,
    val stressStats: StressStatsDto,
    val travelStats: TravelStatsDto,
    val weatherStats: WeatherStatsBo
) {
    data class AlcoholStatsDto(val isPossible: Boolean = false, val beerType: String)

    data class StressStatsDto(val isPossible: Boolean = false, val level: Int)

    data class TravelStatsDto(val isPossible: Boolean = false, val city: String)

    data class WeatherStatsBo(
        val temperatureStats: TemperatureStatsBo,
        val humidityStats: HumidityStatsBo
    )

    data class TemperatureStatsBo(val isPossible: Boolean = false, val level: Int)

    data class HumidityStatsBo(val isPossible: Boolean = false, val level: Int)

    fun toPossibleCauses() = PossibleCausesBO(
        enoughData = relevantLogs > DEFAULT_MIN_LOGS,
        dietaryCauses = emptyList(),
        alcoholCause = alcoholStats.isPossible,
        mostAffectedZones = listOf(),
        stressCause = PossibleCausesBO.StressCauseBO(stressStats.isPossible, stressStats.level),
        travelCause = PossibleCausesBO.TravelCauseBO(travelStats.isPossible, travelStats.city),
        weatherCause = Pair(
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                weatherStats.temperatureStats.isPossible,
                weatherStats.temperatureStats.level
            ), PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                weatherStats.humidityStats.isPossible,
                weatherStats.humidityStats.level
            )
        )

    )


}

