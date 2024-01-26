package com.p4r4d0x.domain.bo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PossibleCausesBO(
    val dietaryCauses: List<String>,
    val alcoholCause: Boolean,
    val mostAffectedZones: List<String>,
    val stressCause: StressCauseBO,
    val travelCause: TravelCauseBO,
    val weatherCause: Pair<WeatherCauseBO, WeatherCauseBO>
) {

    @JsonClass(generateAdapter = true)
    data class StressCauseBO(val possibleCause: Boolean)

    @JsonClass(generateAdapter = true)
    data class TravelCauseBO(val possibleCause: Boolean, val city: String?)

    @JsonClass(generateAdapter = true)
    data class WeatherCauseBO(
        val weatherType: WeatherType,
        val possibleCause: Boolean,
        val averageValue: Int
    ) {
        enum class WeatherType {
            TEMPERATURE, HUMIDITY
        }
    }
}