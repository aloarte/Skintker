package com.p4r4d0x.domain.bo

data class AdditionalDataBO(
    val stressLevel: Int,
    val weather: WeatherBO,
    val travel: TravelBO,
    val alcohol: AlcoholBO
) {
    data class WeatherBO(val humidity: Int, val temperature: Int)
    data class TravelBO(val traveled: Boolean, val city: String)

    data class AlcoholBO(
        val level: AlcoholLevel,
        val beers: List<String> = emptyList(),
        val wines: List<String> = emptyList(),
        val distilledDrinks: List<String> = emptyList())
}