package com.p4r4d0x.data.dto

import com.p4r4d0x.domain.bo.AlcoholLevel

data class AdditionalDataDto(
    val stressLevel: Int,
    val weather: WeatherDto,
    val travel: TravelDto,
    val alcoholLevel: AlcoholLevel,
    val beerTypes: List<String>
)