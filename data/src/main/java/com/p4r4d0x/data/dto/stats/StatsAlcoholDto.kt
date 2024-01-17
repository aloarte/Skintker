package com.p4r4d0x.data.dto.stats

import com.p4r4d0x.domain.bo.AlcoholLevel


data class StatsAlcoholDto(
    val isPossible: Boolean = false,
    val type: AlcoholLevel = AlcoholLevel.None,
    val suspiciousBeers: List<String> = emptyList(),
    val suspiciousWines: List<String> = emptyList(),
    val suspiciousDrinks: List<String> = emptyList()

)

