package com.p4r4d0x.domain.bo

data class AdditionalDataBO(

    val stressLevel: Int,
    val weather: WeatherBO,
    val travel: TravelBO,
    val alcoholLevel: AlcoholLevel,
    val beerTypes: List<String> = emptyList()
) {
    data class WeatherBO(val humidity: Int, val temperature: Int)
    data class TravelBO(val traveled: Boolean, val city: String)
}