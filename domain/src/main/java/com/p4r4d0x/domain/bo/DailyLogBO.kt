package com.p4r4d0x.domain.bo

import java.util.*

data class DailyLogBO(
    val date: Date,
    val irritation: IrritationBO,
    val additionalData: AdditionalDataBO,
    val foodList: List<String>
)