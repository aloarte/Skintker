package com.p4r4d0x.skintker.domain

import java.util.*

class DailyLogBO(
    val date: Date,
    val irritation: IrritationBO,
    val fooodSchedule: FoodScheduleBO,
    val additionalData: AdditionalDataBO
)