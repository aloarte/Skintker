package com.p4r4d0x.domain

import com.p4r4d0x.domain.bo.*
import java.util.*

object TestData {

    const val USER_ID = "user_id"

    val date = Date()

    private val additionalData = AdditionalDataBO(
        stressLevel = 7,
        alcohol = AlcoholBO(level = AlcoholLevel.None),
        weather = WeatherBO(
            humidity = 2,
            temperature = 1
        ),
        travel = TravelBO(
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
        dietaryCauses = emptyList(),
        alcoholCause = true,
        mostAffectedZones = emptyList(),
        stressCause = PossibleCausesBO.StressCauseBO(false),
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