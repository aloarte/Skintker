package com.p4r4d0x.skintker.domain.usecases

import com.p4r4d0x.skintker.data.repository.LogManagementRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.PossibleCausesBO

class GetQueriedLogsUseCase(private val repository: LogManagementRepository) :
    BaseUseCaseParamsResult<GetQueriedLogsUseCase.Params, PossibleCausesBO>() {

    override suspend fun run(params: Params): PossibleCausesBO {
        val logs = repository.getLogsByIrritationLevel(params.irritationLevel)
        return getPossibleCauses(logs, params)
    }

    private fun getPossibleCauses(logs: List<DailyLogBO>, params: Params): PossibleCausesBO {
        val foodMap = mutableMapOf<String, Int>()
        val zonesMap = mutableMapOf<String, Int>()
        val stressMap = mutableMapOf<Int, Int>()

        logs.forEach { log ->
            log.foodList.forEach { food ->
                foodMap[food] = foodMap[food] ?: 0 + 1
            }
            log.irritation?.zoneValues?.forEach { irritatedZone ->
                zonesMap[irritatedZone.name] = foodMap[irritatedZone.name] ?: 0 + 1
            }
            log.additionalData?.let { additionalData ->
                stressMap[additionalData.stressLevel] =
                    stressMap[additionalData.stressLevel] ?: 0 + 1

            }

        }

        return PossibleCausesBO(
            dietaryCauses = getPossibleCausesItemList(
                itemMap = foodMap,
                threshold = params.foodThreshold
            ),
            mostAffectedZones = getPossibleCausesItemList(
                itemMap = foodMap,
                threshold = params.zonesThreshold
            ),
            stressCause = getPossibleStressCauses(
                stressMap = stressMap,
                stressThresholds = params.stressThresholds
            ),
            travelCause = getPossibleTravelCauses(logs),
            weatherCause = getPossibleWeatherCauses(logs)
        )
    }

    private fun getPossibleCausesItemList(itemMap: Map<String, Int>, threshold: Int): List<String> {
        val returnList = mutableListOf<String>()
        for ((food, times) in itemMap) {
            if (times >= threshold) {
                returnList.add(food)
            }
        }
        return returnList
    }

    private fun getPossibleStressCauses(
        stressMap: Map<Int, Int>,
        stressThresholds: Pair<Int, Float>
    ): PossibleCausesBO.StressCauseBO {
        val threshold = stressThresholds.first
        val averageThreshold = stressThresholds.second
        var totalRepetitions = 0
        var stressfulRepetitions = 0

        stressMap.forEach { (stressLevel, times) ->
            if (stressLevel > threshold) {
                stressfulRepetitions += times
            }
            totalRepetitions += times
        }

        val possibleCause = stressfulRepetitions >= (totalRepetitions * averageThreshold)
        return PossibleCausesBO.StressCauseBO(
            averageLevel = stressMap.maxByOrNull { it.key }?.key ?: -1,
            possibleCause = possibleCause
        )
    }

    private fun getPossibleTravelCauses(logs: List<DailyLogBO>): PossibleCausesBO.TravelCauseBO {
        return PossibleCausesBO.TravelCauseBO(false, null)
    }

    private fun getPossibleWeatherCauses(logs: List<DailyLogBO>): Pair<PossibleCausesBO.WeatherCauseBO, PossibleCausesBO.WeatherCauseBO> {
        return Pair(
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                true,
                1
            ),
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                true,
                1
            )
        )
    }

    data class Params(
        val irritationLevel: Int,
        val foodThreshold: Int,
        val zonesThreshold: Int,
        val stressThresholds: Pair<Int, Float>,
        val travelThresholds: Pair<Int, Float>,
        val weatherThresholds: Pair<Int, Float>

    )

}
