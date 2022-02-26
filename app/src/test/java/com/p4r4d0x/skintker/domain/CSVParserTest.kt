package com.p4r4d0x.skintker.domain

import com.p4r4d0x.skintker.data.Constants.LABEL_ALCOHOL
import com.p4r4d0x.skintker.data.Constants.LABEL_BEERS
import com.p4r4d0x.skintker.data.Constants.LABEL_CITY
import com.p4r4d0x.skintker.data.Constants.LABEL_DATE
import com.p4r4d0x.skintker.data.Constants.LABEL_FOODS
import com.p4r4d0x.skintker.data.Constants.LABEL_ID
import com.p4r4d0x.skintker.data.Constants.LABEL_IRRITATED_ZONES
import com.p4r4d0x.skintker.data.Constants.LABEL_IRRITATION
import com.p4r4d0x.skintker.data.Constants.LABEL_STRESS
import com.p4r4d0x.skintker.data.Constants.LABEL_TRAVELED
import com.p4r4d0x.skintker.data.Constants.LABEL_WEATHER_HUMIDITY
import com.p4r4d0x.skintker.data.Constants.LABEL_WEATHER_TEMPERATURE
import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
import com.p4r4d0x.skintker.domain.parsers.CSVParser.getCSVRowFromData
import com.p4r4d0x.skintker.domain.parsers.CSVParser.getHeaderCSVRow
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class CSVParserTest {

    companion object {
        val date = Date(1641031810L * 1000) // 2022/01/01 10:10:10
        private const val FOOD_1 = "Banana"
        private const val FOOD_2 = "Apple"
        private const val FOOD_3 = "Fish"
        private const val ZONE_1 = "Leg"
        private const val ZONE_2 = "Shoulder"
        private const val BEER_TYPE_1 = "Lager"
        private const val BEER_TYPE_2 = "Porter"
        val referenceZonesList = mapOf(ZONE_1 to 0, ZONE_2 to 1)
        val referenceFoodList = mapOf(FOOD_1 to 0, FOOD_2 to 1, FOOD_3 to 2)
        val referenceBeerList = mapOf(BEER_TYPE_1 to 0, BEER_TYPE_2 to 1)
    }

    @Before
    fun setup() {

    }

    @Test
    fun `get header CSV row`() {
        val headerList = getHeaderCSVRow(referenceZonesList, referenceFoodList, referenceBeerList)

        val expectedList = listOf(
            LABEL_ID,
            LABEL_DATE,
            LABEL_FOODS,
            LABEL_IRRITATION,
            LABEL_IRRITATED_ZONES,
            LABEL_ALCOHOL,
            LABEL_BEERS,
            LABEL_STRESS,
            LABEL_WEATHER_HUMIDITY,
            LABEL_WEATHER_TEMPERATURE,
            LABEL_CITY,
            LABEL_TRAVELED,
            ZONE_1, ZONE_2,
            FOOD_1, FOOD_2, FOOD_3,
            BEER_TYPE_1, BEER_TYPE_2
        )

        assertEquals(expectedList, headerList)
    }

    @Test
    fun `get data CSV row`() {
        val dailyLog = DailyLogBO(
            date = date,

            foodList = listOf(FOOD_1, FOOD_3),
            additionalData = AdditionalDataBO(
                alcoholLevel = AlcoholLevel.Few,
                weather = AdditionalDataBO.WeatherBO(temperature = 3, humidity = 4),
                travel = AdditionalDataBO.TravelBO(city = "Madrid", traveled = true),
                stressLevel = 10
            ),
            irritation = IrritationBO(overallValue = 10, zoneValues = listOf(ZONE_1))
        )

        val dataRow = getCSVRowFromData(
            index = 2,
            log = dailyLog,
            referenceZonesList = referenceZonesList,
            referenceFoodList = referenceFoodList,
            referenceBeerTypesList = referenceBeerList
        )

        val expectedList = listOf(
            "2",
            "01/01/2022",
            "$FOOD_1,$FOOD_3",
            "10",
            ZONE_1,
            "Few",
            "",
            "10",
            "4",
            "3",
            "Madrid",
            "true",
            ZONE_1, "",
            FOOD_1, "", FOOD_3,
            "", ""
        )
        assertEquals(expectedList, dataRow)
    }


}