package com.p4r4d0x.domain

import com.p4r4d0x.domain.bo.*
import java.util.*

object TestData {

    const val USER_ID = "user_id"

    val date = Date()

    private val additionalData = AdditionalDataBO(
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

    private val irritationData = IrritationBO(1, emptyList())

    val log = DailyLogBO(
        date,
        additionalData = additionalData,
        irritation = irritationData,
        foodList = emptyList()
    )

    val stats = PossibleCausesBO(
        enoughData = false,
        dietaryCauses = emptyList(),
        alcoholCause = true,
        mostAffectedZones = emptyList(),
        stressCause = PossibleCausesBO.StressCauseBO(false, 4),
        travelCause = PossibleCausesBO.TravelCauseBO(false, "Madrid"),
        weatherCause = Pair(
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                true,
                4
            ),
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                true,
                1
            )
        )
    )
}