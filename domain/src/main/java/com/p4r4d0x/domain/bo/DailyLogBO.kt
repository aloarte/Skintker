package com.p4r4d0x.domain.bo

import java.util.*

data class DailyLogBO(
    val date: Date,
    val irritation: IrritationBO? = null,
    val additionalData: AdditionalDataBO? = null,
    val foodList: List<String>
)