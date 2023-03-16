package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.repository.StatsRepository

class GetQueriedLogsUseCase(
    private val repository: StatsRepository
) :
    BaseUseCaseParamsResult<GetQueriedLogsUseCase.Params, PossibleCausesBO?>() {

    override suspend fun run(params: Params): PossibleCausesBO? {
        return repository.getStats(params.userId)
    }

    data class Params(val userId: String)
}