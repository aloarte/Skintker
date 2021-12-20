package com.p4r4d0x.skintker.domain.bo

import java.util.*

class DailyLogBO(
    val date: Date,
    val irritation: IrritationBO,
    val foodSchedule: FoodScheduleBO,
    val additionalData: AdditionalDataBO
)