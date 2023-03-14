package com.p4r4d0x.domain

import com.p4r4d0x.domain.bo.AdditionalDataBO
import com.p4r4d0x.domain.bo.AlcoholLevel
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.IrritationBO
import java.util.*

object TestData {

    const val USER_ID = "user_id"

    val date = Date()

    val additionalData = AdditionalDataBO(
        stressLevel = 7,
        alcoholLevel = AlcoholLevel.Few,
        weather = AdditionalDataBO.WeatherBO(
            humidity = 2,
            temperature = 1
        ),
        travel = AdditionalDataBO.TravelBO(
            traveled = true,
            city = "Madrid"
        )
    )

    val irritationData = IrritationBO(1, emptyList())

    val log = DailyLogBO(
        date,
        additionalData = additionalData,
        irritation = irritationData,
        foodList = emptyList()
    )
}