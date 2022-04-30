package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.bo.AlcoholLevel
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.repository.LogsManagementRepository
import com.p4r4d0x.domain.utils.CausesParser.getAlcoholCause
import com.p4r4d0x.domain.utils.CausesParser.getPossibleCausesItemList
import com.p4r4d0x.domain.utils.CausesParser.getPossibleStressCauses
import com.p4r4d0x.domain.utils.CausesParser.getPossibleTravelCauses
import com.p4r4d0x.domain.utils.CausesParser.getPossibleWeatherCauses
import com.p4r4d0x.domain.utils.increaseValue

class GetQueriedLogsUseCase(private val repository: LogsManagementRepository) :
    BaseUseCaseParamsResult<GetQueriedLogsUseCase.Params, PossibleCausesBO>() {

    override suspend fun run(params: Params): PossibleCausesBO {
        val logs = repository.getLogsByIrritationLevel(params.irritationLevel)
        return getPossibleCauses(logs, params)
    }

    private fun getPossibleCauses(logs: List<DailyLogBO>, params: Params): PossibleCausesBO {
        val foodMap = mutableMapOf<String, Int>()
        val zonesMap = mutableMapOf<String, Int>()
        val stressMap = mutableMapOf<Int, Int>()
        val traveledMap = mutableMapOf<Boolean, Int>()
        val traveledCityMap = mutableMapOf<String, Int>()
        val temperatureMap = mutableMapOf<Int, Int>()
        val humidityCityMap = mutableMapOf<Int, Int>()
        val alcoholLevelMap = mutableMapOf<AlcoholLevel, Int>()

        logs.forEach { log ->
            log.foodList.forEach { food ->
                foodMap.increaseValue(food)
            }
            log.irritation?.zoneValues?.forEach { irritatedZone ->
                zonesMap.increaseValue(irritatedZone)
            }
            log.additionalData?.let { additionalData ->
                stressMap.increaseValue(additionalData.stressLevel)
                traveledMap.increaseValue(additionalData.travel.traveled)
                traveledCityMap.increaseValue(additionalData.travel.city)
                temperatureMap.increaseValue(additionalData.weather.temperature)
                humidityCityMap.increaseValue(additionalData.weather.humidity)
                alcoholLevelMap.increaseValue(additionalData.alcoholLevel)
            }
        }

        return PossibleCausesBO(
            enoughData = logs.size >= params.minLogs,
            dietaryCauses = getPossibleCausesItemList(
                itemMap = foodMap,
                threshold = params.foodThreshold
            ),
            alcoholCause = getAlcoholCause(alcoholLevelMap, params.alcoholThreshold),
            mostAffectedZones = getPossibleCausesItemList(
                itemMap = foodMap,
                threshold = params.zonesThreshold
            ),
            stressCause = getPossibleStressCauses(
                stressMap = stressMap,
                stressThresholds = params.stressThresholds
            ),
            travelCause = getPossibleTravelCauses(
                traveledMap = traveledMap,
                traveledCityMap = traveledCityMap,
                thresholds = params.travelThreshold
            ),
            weatherCause = getPossibleWeatherCauses(
                temperatureMap = temperatureMap,
                humidityMap = humidityCityMap,
                thresholds = params.weatherThresholds
            )
        )
    }

    data class Params(
        val irritationLevel: Int,
        val minLogs: Int,
        val foodThreshold: Float,
        val zonesThreshold: Float,
        val alcoholThreshold: Float,
        val travelThreshold: Float,
        val stressThresholds: Pair<Int, Float>,
        val weatherThresholds: Pair<Float, Float>
    )
}