package com.p4r4d0x.skintker.domain.usecases

import com.p4r4d0x.skintker.data.repository.LogManagementRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.PossibleCausesBO

class GetQueriedLogsUseCase(private val repository: LogManagementRepository) :
    BaseUseCaseParamsResult<GetQueriedLogsUseCase.Params, PossibleCausesBO>() {

    override suspend fun run(params: Params): PossibleCausesBO {

        val logs = repository.getLogsByIrritationLevel(params.irritationLevel)

        return PossibleCausesBO(
            dietaryCauses = getPossibleDietaryCauses(logs),
            stressCause = getPossibleStressCauses(logs),
            travelCause = getPossibleTravelCauses(logs),
            weatherCause = getPossibleWeatherCauses(logs)
        )
    }

    private fun getPossibleDietaryCauses(logs: List<DailyLogBO>): List<String> {
        TODO()
    }

    private fun getPossibleStressCauses(logs: List<DailyLogBO>): PossibleCausesBO.StressCauseBO {
        TODO()
    }

    private fun getPossibleTravelCauses(logs: List<DailyLogBO>): PossibleCausesBO.TravelCauseBO {
        TODO()
    }

    private fun getPossibleWeatherCauses(logs: List<DailyLogBO>): Pair<PossibleCausesBO.WeatherCauseBO, PossibleCausesBO.WeatherCauseBO> {
        TODO()
    }

    data class Params(val irritationLevel: Int)

}
