package com.p4r4d0x.data.testutils

import com.p4r4d0x.data.dto.logs.ReportDto
import com.p4r4d0x.data.dto.stats.StatsAlcoholDto
import com.p4r4d0x.data.dto.stats.StatsDto
import com.p4r4d0x.data.dto.stats.StatsTravelDto
import com.p4r4d0x.data.dto.stats.StatsWeatherDto
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

    val logDto = ReportDto(
        date = "09-02-2023",
        irritation = ReportDto.IrritationDto(9, listOf("chest", "ears")),
        additionalData = ReportDto.AdditionalDataDto(
            stressLevel = 4,
            weather = ReportDto.WeatherDto(humidity = 1, temperature = 4),
            travel = ReportDto.TravelDto(city = "Madrid", traveled = false),
            alcohol = ReportDto.AlcoholDto(
                level = AlcoholLevel.Beer,
                beers = listOf("wheat", "stout")
            ),
        ),
        foodList = listOf("banana", "fish", "meat")
    )

    val log = DailyLogBO(
        DataParser.backendStringToDate("09-02-2023"),
        irritation = IrritationBO(9, listOf("chest", "ears")),
        additionalData = AdditionalDataBO(
            stressLevel = 4,
            weather = AdditionalDataBO.WeatherBO(humidity = 1, temperature = 4),
            travel = AdditionalDataBO.TravelBO(city = "Madrid", traveled = false),
            alcohol = AdditionalDataBO.AlcoholBO(
                level = AlcoholLevel.Beer,
                beers = listOf("wheat", "stout")
            ),
        ),
        foodList = listOf("banana", "fish", "meat")
    )

    val stats = PossibleCausesBO(
        dietaryCauses = listOf("Meat","Blue fish"),
        alcoholCause = true,
        mostAffectedZones = listOf("IrritationZone"),
        stressCause = PossibleCausesBO.StressCauseBO(true),
        travelCause = PossibleCausesBO.TravelCauseBO(true, "Madrid"),
        weatherCause = Pair(
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                false,
                0
            ),
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                false,
                5
            )
        )
    )

    val completeStatsBo = PossibleCausesBO(
        dietaryCauses = listOf("Wheat,Meat,Fish"),
        alcoholCause = true,
        mostAffectedZones = listOf("Shoulders,Neck"),
        stressCause = PossibleCausesBO.StressCauseBO(true),
        travelCause = PossibleCausesBO.TravelCauseBO(true, "Madrid"),
        weatherCause = Pair(
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                true,
                1
            ),
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                true,
                2
            )
        )
    )

    val incompleteStatsBo = PossibleCausesBO(
        dietaryCauses = emptyList(),
        alcoholCause = false,
        mostAffectedZones = emptyList(),
        stressCause = PossibleCausesBO.StressCauseBO(false),
        travelCause = PossibleCausesBO.TravelCauseBO(false, city = null),
        weatherCause = Pair(
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                false,
                -1
            ),
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                false,
                -1
            )
        )
    )

    val completeStatsDto = StatsDto(
        dietaryCauses = listOf("Wheat,Meat,Fish"),
        mostAffectedZones = listOf("Shoulders,Neck"),
        alcohol = StatsAlcoholDto(true, suspiciousBeers = listOf("Porter")),
        stress = true,
        travel = StatsTravelDto(true, "Madrid"),
        weather = StatsWeatherDto(
            StatsWeatherDto.StatsTemperature(true, 1),
            StatsWeatherDto.StatsHumidity(true, 2)
        )
    )

    val incompleteStatsDto = StatsDto(
        alcohol = StatsAlcoholDto(),
        stress = false,
        travel = StatsTravelDto(),
        weather = StatsWeatherDto()
    )
}