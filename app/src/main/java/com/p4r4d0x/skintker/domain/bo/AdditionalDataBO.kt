package com.p4r4d0x.skintker.domain.bo

import com.p4r4d0x.skintker.data.AlcoholLevel

class AdditionalDataBO(
    val stressLevel: Int,
    val weather: WeatherBO,
    val travel: TravelBO,
    val alcoholLevel: AlcoholLevel
) {
    class WeatherBO(val humidity: Int, val temperature: Int)
    class TravelBO(val traveled: Boolean, val city: String)
}