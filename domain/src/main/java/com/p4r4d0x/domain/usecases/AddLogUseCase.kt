package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.ReportStatus
import com.p4r4d0x.domain.repository.ReportsManagementRepository

class AddLogUseCase(private val repository: ReportsManagementRepository) :
    BaseUseCaseParamsResult<AddLogUseCase.Params, Boolean>() {

    override suspend fun run(params: Params): Boolean {
        val logExist = repository.getReport(params.log.date) != null
        val reportAdded = if (logExist) {
            repository.editReport(params.userId, params.log)
        } else {
            repository.addReport(params.userId, params.log)
        }

        return when (reportAdded) {
            ReportStatus.Created -> true
            ReportStatus.Edited -> true
            ReportStatus.Error -> false
        }

    }

    data class Params(val userId: String, val log: DailyLogBO)

}
