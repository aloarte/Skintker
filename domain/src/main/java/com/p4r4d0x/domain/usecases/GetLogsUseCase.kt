package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.repository.ReportsManagementRepository

class GetLogsUseCase(
    private val reportsMgmtRepository: ReportsManagementRepository
) :
    BaseUseCaseParamsResult<GetLogsUseCase.Params, List<DailyLogBO>>() {

    data class Params(val user: String)

    override suspend fun run(params: Params): List<DailyLogBO> {
        return reportsMgmtRepository.getReports(params.user).logList
    }
}
