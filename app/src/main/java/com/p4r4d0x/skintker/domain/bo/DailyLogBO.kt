package com.p4r4d0x.skintker.domain.bo

import java.util.*

data class DailyLogBO(
    val date: Date,
    val irritation: IrritationBO? = null,
    val foodSchedule: FoodScheduleBO? = null,
    val additionalData: AdditionalDataBO? = null
)