package com.p4r4d0x.data.testutils

import com.p4r4d0x.data.dto.logs.ReportDto
import com.p4r4d0x.data.dto.stats.StatsDto
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
            beerTypes = listOf("wheat", "stout"),
            alcoholLevel = AlcoholLevel.Few
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
            beerTypes = listOf("wheat", "stout"),
            alcoholLevel = AlcoholLevel.Few
        ),
        foodList = listOf("banana", "fish", "meat")
    )

    val stats = PossibleCausesBO(
        enoughData = true,
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

    val completeStatsBo = PossibleCausesBO(
        enoughData = true,
        dietaryCauses = listOf("Wheat,Meat,Fish"),
        alcoholCause = true,
        mostAffectedZones = listOf("Shoulders,Neck"),
        stressCause = PossibleCausesBO.StressCauseBO(true, 7),
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
        enoughData = true,
        dietaryCauses = emptyList(),
        alcoholCause = false,
        mostAffectedZones = emptyList(),
        stressCause = PossibleCausesBO.StressCauseBO(false, 0),
        travelCause = PossibleCausesBO.TravelCauseBO(false, city = null),
        weatherCause = Pair(
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                false,
                0
            ),
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                false,
                0
            )
        )
    )

    val completeStatsDto = StatsDto(
        relevantLogs = 12,
        dietaryCauses = listOf("Wheat,Meat,Fish"),
        mostAffectedZones = listOf("Shoulders,Neck"),
        alcohol = StatsDto.AlcoholStatsDto(true, ""),
        stress = StatsDto.StressStatsDto(true, 7),
        travel = StatsDto.TravelStatsDto(true, "Madrid"),
        weather = StatsDto.WeatherStatsDto(
            StatsDto.TemperatureStatsDto(true, 1),
            StatsDto.HumidityStatsDto(true, 2)
        )
    )

    val incompleteStatsDto = StatsDto(
        relevantLogs = 12,
        alcohol = null,
        stress = null,
        travel = null,
        weather = null
    )
}