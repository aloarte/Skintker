package com.p4r4d0x.domain.utils

import com.p4r4d0x.domain.bo.AlcoholLevel
import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.utils.CausesParser.getAlcoholCause
import com.p4r4d0x.domain.utils.CausesParser.getPossibleCausesItemList
import com.p4r4d0x.domain.utils.CausesParser.getPossibleStressCauses
import com.p4r4d0x.domain.utils.CausesParser.getPossibleTravelCauses
import com.p4r4d0x.domain.utils.CausesParser.getPossibleWeatherCauses
import org.junit.Assert
import org.junit.Test

class CausesParserTest {

    @Test
    fun `get possible causes item list`() {
        val itemMap = mapOf(
            "Item1" to 20,
            "Item2" to 30,
            "Item3" to 5,
            "Item4" to 5,
            "Item5" to 10,
            "Item6" to 40
        )

        val causesList = getPossibleCausesItemList(itemMap, 0.2f)

        val expectedList = listOf("Item2", "Item6")
        Assert.assertEquals(expectedList, causesList)
    }

    @Test
    fun `get possible stress causes`() {
        val map = mapOf(
            6 to 30,
            4 to 30,
            5 to 10,
            8 to 10,
            9 to 5,
            1 to 5,
            2 to 5,
            3 to 5,
            7 to 0,
            10 to 0
        )

        val causes = getPossibleStressCauses(stressMap = map, stressThresholds = Pair(6, 0.6f))

        val expectedValue = PossibleCausesBO.StressCauseBO(false, 6)
        Assert.assertEquals(expectedValue, causes)
    }

    @Test
    fun `get possible  travel causes`() {
        val travelMap = mapOf(true to 10, false to 90)
        val cityMap =
            mapOf(
                "Sidney" to 5,
                "NewYork" to 5,
                "Madrid" to 40,
                "Barcelona" to 40,
                "London" to 10
            )

        val causes = getPossibleTravelCauses(
            traveledMap = travelMap,
            traveledCityMap = cityMap,
            thresholds = 0.6f
        )

        val expectedValue = PossibleCausesBO.TravelCauseBO(false, "Madrid")
        Assert.assertEquals(expectedValue, causes)
    }

    @Test
    fun `get possible weather causes`() {
        val temperatureMap = mapOf(
            1 to 10,
            2 to 50,
            3 to 25,
            4 to 15,
            5 to 0
        )
        val humidityCityMap = mapOf(
            1 to 25,
            2 to 25,
            3 to 20,
            4 to 5,
            5 to 25
        )

        val causes = getPossibleWeatherCauses(
            temperatureMap = temperatureMap,
            humidityMap = humidityCityMap,
            thresholds = Pair(0.5f, 0.5f)
        )

        val expectedValue = Pair(
            PossibleCausesBO.WeatherCauseBO(
                weatherType = PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                possibleCause = true,
                averageValue = 2
            ), PossibleCausesBO.WeatherCauseBO(
                weatherType = PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                possibleCause = true,
                averageValue = 5
            )
        )
        Assert.assertEquals(expectedValue, causes)
    }

    @Test
    fun `get alcohol cause`() {
        val alcoholMap = mapOf(
            AlcoholLevel.None to 30,
            AlcoholLevel.Few to 50,
            AlcoholLevel.FewWine to 10,
            AlcoholLevel.Some to 10,
        )

        val cause = getAlcoholCause(alcoholLevelMap = alcoholMap, alcoholThreshold = 0.7f)

        Assert.assertEquals(true, cause)
    }

}