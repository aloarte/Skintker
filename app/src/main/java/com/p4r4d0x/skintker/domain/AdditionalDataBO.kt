package com.p4r4d0x.skintker.domain

class AdditionalDataBO(
    val stressLevel: Int,
    val weatherStatus: WeatherStatus
)

enum class WeatherStatus {
    SUNNY, CLOUDY, RAINY, SNOWY
}
