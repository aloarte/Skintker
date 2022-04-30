package com.p4r4d0x.domain.utils

import com.p4r4d0x.domain.bo.AlcoholLevel
import com.p4r4d0x.domain.bo.PossibleCausesBO

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
        if (stressMap.isEmpty()) {
            return PossibleCausesBO.StressCauseBO(
                possibleCause = false,
                averageLevel = -1
            )
        }
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
        if (traveledMap.isEmpty() || traveledCityMap.isEmpty()) {
            return PossibleCausesBO.TravelCauseBO(false, null)
        }
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
        humidityMap: Map<Int, Int>,
        thresholds: Pair<Float, Float>
    ): Pair<PossibleCausesBO.WeatherCauseBO, PossibleCausesBO.WeatherCauseBO> {
        return if (temperatureMap.isEmpty() || humidityMap.isEmpty()) {
            Pair(
                PossibleCausesBO.WeatherCauseBO(
                    weatherType = PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                    possibleCause = false,
                    averageValue = -1
                ),
                PossibleCausesBO.WeatherCauseBO(
                    weatherType = PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                    possibleCause = false,
                    averageValue = -1
                )
            )
        } else {
            Pair(
                PossibleCausesBO.WeatherCauseBO(
                    weatherType = PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                    possibleCause = temperatureMap.getKeyOfMaxValue() > temperatureMap.size * thresholds.first,
                    averageValue = temperatureMap.getMaxValue()
                ),
                PossibleCausesBO.WeatherCauseBO(
                    weatherType = PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                    possibleCause = humidityMap.getKeyOfMaxValue() > humidityMap.size * thresholds.first,
                    averageValue = humidityMap.getMaxValue()
                )
            )
        }
    }


    fun getAlcoholCause(alcoholLevelMap: Map<AlcoholLevel, Int>, alcoholThreshold: Float): Boolean {
        if (alcoholLevelMap.isEmpty()) {
            return false
        }
        var alcoholRepetitions = 0
        var totalRepetitions = 0

        alcoholLevelMap.forEach { (alcoholLevel, times) ->
            if (alcoholLevel != AlcoholLevel.None) {
                alcoholRepetitions += times
            }
            totalRepetitions += times
        }

        return alcoholRepetitions >= totalRepetitions * alcoholThreshold
    }

}