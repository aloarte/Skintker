package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.repository.ReportsManagementRepository

class RemoveLogsUseCase(
    private val reportsMgmtRepository: ReportsManagementRepository
) : BaseUseCaseParamsResult<RemoveLogsUseCase.Params, Boolean>() {

    override suspend fun run(params: Params): Boolean {
        return reportsMgmtRepository.deleteReports(params.userId)
    }

    data class Params(val userId: String)

}
