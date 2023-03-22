package com.p4r4d0x.data.testutils

import com.p4r4d0x.data.parsers.DataParser
import com.p4r4d0x.domain.bo.*
import java.util.*

object TestData {
    const val USER_ID = "userId"
    const val REPORT_DATE = "09-02-2023"
    const val OFFSET = 0
    const val LIMIT = 4
    val date = Date()
    val stringDate = DataParser.backendDateToString(date)

    val log = DailyLogBO(
        DataParser.backendStringToDate("09-02-2023"),
        irritation = IrritationBO(9, listOf("chest", "ears")),
        additionalData = AdditionalDataBO(
            stressLevel = 4,
            weather = AdditionalDataBO.WeatherBO(humidity = 1, temperature = 4),
            travel = AdditionalDataBO.TravelBO(city = "Madrid", traveled = false),
            beerTypes = listOf("wheat", "stout"),
            alcoholLevel = AlcoholLevel.Few
        ),
        foodList = listOf("banana", "fish", "meat")
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