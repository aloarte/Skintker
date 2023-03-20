package com.p4r4d0x.data.dto.stats

import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.utils.Constants.DEFAULT_MIN_LOGS

data class StatsDto(
    val relevantLogs: Int,
    val alcohol: AlcoholStatsDto,
    val stress: StressStatsDto,
    val travel: TravelStatsDto,
    val weather: WeatherStatsBo
) {
    data class AlcoholStatsDto(val isPossible: Boolean = false, val beerType: String)

    data class StressStatsDto(val isPossible: Boolean = false, val level: Int)

    data class TravelStatsDto(val isPossible: Boolean = false, val city: String)

    data class WeatherStatsBo(
        val temperature: TemperatureStatsBo,
        val humidity: HumidityStatsBo
    )

    data class TemperatureStatsBo(val isPossible: Boolean = false, val level: Int)

    data class HumidityStatsBo(val isPossible: Boolean = false, val level: Int)

    fun toPossibleCauses() = PossibleCausesBO(
        enoughData = relevantLogs > DEFAULT_MIN_LOGS,
        dietaryCauses = emptyList(),
        alcoholCause = alcohol.isPossible,
        mostAffectedZones = listOf(),
        stressCause = PossibleCausesBO.StressCauseBO(stress.isPossible, stress.level),
        travelCause = PossibleCausesBO.TravelCauseBO(travel.isPossible, travel.city),
        weatherCause = Pair(
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                weather.temperature.isPossible,
                weather.temperature.level
            ), PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                weather.humidity.isPossible,
                weather.humidity.level
            )
        )

    )


}

