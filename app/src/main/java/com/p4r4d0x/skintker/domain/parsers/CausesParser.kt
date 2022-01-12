package com.p4r4d0x.skintker.domain.parsers

import com.p4r4d0x.skintker.domain.bo.PossibleCausesBO
import com.p4r4d0x.skintker.domain.getKeyOfMaxValue
import com.p4r4d0x.skintker.domain.getMaxValue

object CausesParser {

    /**
     * Parse the given map of <string item , times> returning the list of the most repeated items
     * that get over the given threshold.
     */
    fun getPossibleCausesItemList(itemMap: Map<String, Int>, threshold: Float): List<String> {
        val returnList = mutableListOf<String>()
        var totalCnt = 0
        for ((_, times) in itemMap) {
            totalCnt += times
        }
        for ((food, times) in itemMap) {
            if (times >= totalCnt * threshold) {
                returnList.add(food)
            }
        }
        return returnList
    }

    /**
     * Parse the given map of <stress integer value , times> returning a StressCauseBO using the
     * given thresholds.
     */
    fun getPossibleStressCauses(
        stressMap: Map<Int, Int>,
        stressThresholds: Pair<Int, Float>
    ): PossibleCausesBO.StressCauseBO {
        val threshold = stressThresholds.first
        val averageThreshold = stressThresholds.second
        var totalRepetitions = 0
        var stressfulRepetitions = 0

        stressMap.forEach { (stressLevel, times) ->
            if (stressLevel >= threshold) {
                stressfulRepetitions += times
            }
            totalRepetitions += times
        }

        val possibleCause = stressfulRepetitions >= (totalRepetitions * averageThreshold)
        return PossibleCausesBO.StressCauseBO(
            averageLevel = stressMap.getMaxValue(),
            possibleCause = possibleCause
        )
    }

    /**
     * Parse the given maps of <traveled boolean value , times> and <traveled city string value
     * , times> returning a TravelCauseBO using the given thresholds.
     */
    fun getPossibleTravelCauses(
        traveledMap: Map<Boolean, Int>,
        traveledCityMap: Map<String, Int>,
        thresholds: Float
    ): PossibleCausesBO.TravelCauseBO {
        var travelRepetitions = 0
        var totalRepetitions = 0

        traveledMap.forEach { (traveled, times) ->
            if (traveled) {
                travelRepetitions += times
            }
            totalRepetitions += times
        }

        return PossibleCausesBO.TravelCauseBO(
            possibleCause = travelRepetitions >= (totalRepetitions * thresholds),
            city = traveledCityMap.getMaxValue()
        )
    }

    /**
     * Parse the given maps of <temperature integer value , times> and <humidity integer string
     * value, times> returning a pair of WeatherCauseBO using the given thresholds.
     */
    fun getPossibleWeatherCauses(
        temperatureMap: Map<Int, Int>,
        humidityCityMap: Map<Int, Int>,
        thresholds: Pair<Float, Float>
    ): Pair<PossibleCausesBO.WeatherCauseBO, PossibleCausesBO.WeatherCauseBO> {
        return Pair(
            PossibleCausesBO.WeatherCauseBO(
                weatherType = PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                possibleCause = temperatureMap.getKeyOfMaxValue() > temperatureMap.size * thresholds.first,
                averageValue = temperatureMap.getMaxValue()
            ),
            PossibleCausesBO.WeatherCauseBO(
                weatherType = PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                possibleCause = humidityCityMap.getKeyOfMaxValue() > humidityCityMap.size * thresholds.first,
                averageValue = humidityCityMap.getMaxValue()
            )
        )
    }
}