package com.p4r4d0x.data.dto.stats

import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StatsDto(
    val dietaryCauses: List<String> = emptyList(),
    val mostAffectedZones: List<String> = emptyList(),
    val alcohol: StatsAlcoholDto = StatsAlcoholDto(),
    val stress: Boolean = false,
    val travel: StatsTravelDto = StatsTravelDto(),
    val weather: StatsWeatherDto = StatsWeatherDto()
) {
    fun toPossibleCauses() = PossibleCausesBO(
        dietaryCauses = dietaryCauses,
        alcoholCause = alcohol.isPossible,
        mostAffectedZones = mostAffectedZones,
        stressCause = PossibleCausesBO.StressCauseBO(stress),
        travelCause = PossibleCausesBO.TravelCauseBO(travel.isPossible, travel.city),
        weatherCause = Pair(
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                weather.temperature.isPossible ,
                weather.temperature.level
            ), PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                weather.humidity.isPossible ,
                weather.humidity.level
            )
        )
    )
}
