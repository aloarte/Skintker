package com.p4r4d0x.skintker.domain.usecases

import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import com.p4r4d0x.skintker.data.repository.LogManagementRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.PossibleCausesBO
import com.p4r4d0x.skintker.domain.parsers.CausesParser.getAlcoholCause
import com.p4r4d0x.skintker.domain.parsers.CausesParser.getPossibleCausesItemList
import com.p4r4d0x.skintker.domain.parsers.CausesParser.getPossibleStressCauses
import com.p4r4d0x.skintker.domain.parsers.CausesParser.getPossibleTravelCauses
import com.p4r4d0x.skintker.domain.parsers.CausesParser.getPossibleWeatherCauses
import com.p4r4d0x.skintker.domain.updateValue

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
        val traveledMap = mutableMapOf<Boolean, Int>()
        val traveledCityMap = mutableMapOf<String, Int>()
        val temperatureMap = mutableMapOf<Int, Int>()
        val humidityCityMap = mutableMapOf<Int, Int>()
        val alcoholLevelMap = mutableMapOf<AlcoholLevel, Int>()

        logs.forEach { log ->
            log.foodList.forEach { food ->
                foodMap.updateValue(food)
            }
            log.irritation?.zoneValues?.forEach { irritatedZone ->
                zonesMap.updateValue(irritatedZone.name)
            }
            log.additionalData?.let { additionalData ->
                stressMap.updateValue(additionalData.stressLevel)
                traveledMap.updateValue(additionalData.travel.traveled)
                traveledCityMap.updateValue(additionalData.travel.city)
                temperatureMap.updateValue(additionalData.weather.temperature)
                humidityCityMap.updateValue(additionalData.weather.humidity)
                alcoholLevelMap.updateValue(additionalData.alcoholLevel)
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
                humidityCityMap = humidityCityMap,
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