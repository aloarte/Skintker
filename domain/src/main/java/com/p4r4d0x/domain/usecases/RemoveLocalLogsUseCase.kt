package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.repository.ReportsManagementRepository

class RemoveLocalLogsUseCase(
    private val reportsMgmtRepository: ReportsManagementRepository
) : BaseUseCaseParamsResult<RemoveLocalLogsUseCase.Params, Boolean>() {

    override suspend fun run(params: Params): Boolean {
        return reportsMgmtRepository.deleteLocalReports(params.userId)
    }

    data class Params(val userId: String)

}
