package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.repository.ReportsManagementRepository
import java.util.*

class RemoveLogUseCase(
    private val reportsMgmtRepository: ReportsManagementRepository
) : BaseUseCaseParamsResult<RemoveLogUseCase.Params, Boolean>() {

    override suspend fun run(params: Params): Boolean {
        return reportsMgmtRepository.deleteReport(params.userId, params.logDate)
    }

    data class Params(val userId: String, val logDate: Date)

}
