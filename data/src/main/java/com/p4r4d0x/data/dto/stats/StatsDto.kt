package com.p4r4d0x.data.dto.stats

import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.utils.Constants.DEFAULT_MIN_LOGS

data class StatsDto(
    val enoughData: Boolean = false,
    val relevantLogs: Int,
    val dietaryCauses: List<String>? = emptyList(),
    val mostAffectedZones: List<String>? = emptyList(),
    val alcohol: AlcoholStatsDto?,
    val stress: StressStatsDto?,
    val travel: TravelStatsDto?,
    val weather: WeatherStatsDto?
) {

    data class AlcoholStatsDto(val isPossible: Boolean = false, val beerType: String)

    data class StressStatsDto(val isPossible: Boolean = false, val level: Int)

    data class TravelStatsDto(val isPossible: Boolean = false, val city: String)

    data class WeatherStatsDto(
        val temperature: TemperatureStatsDto,
        val humidity: HumidityStatsDto
    )

    data class TemperatureStatsDto(val isPossible: Boolean = false, val level: Int)

    data class HumidityStatsDto(val isPossible: Boolean = false, val level: Int)

    fun toPossibleCauses() = PossibleCausesBO(
        enoughData = relevantLogs > DEFAULT_MIN_LOGS,
        dietaryCauses = dietaryCauses ?: emptyList(),
        alcoholCause = alcohol?.isPossible ?: false,
        mostAffectedZones = mostAffectedZones ?: emptyList(),
        stressCause = PossibleCausesBO.StressCauseBO(
            stress?.isPossible ?: false,
            stress?.level ?: 0
        ),
        travelCause = PossibleCausesBO.TravelCauseBO(travel?.isPossible ?: false, travel?.city),
        weatherCause = Pair(
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                weather?.temperature?.isPossible ?: false,
                weather?.temperature?.level ?: 0
            ), PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                weather?.humidity?.isPossible ?: false,
                weather?.humidity?.level ?: 0
            )
        )
    )
}

