package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.repository.LogsManagementRepository

class AddLogUseCase(private val repository: LogsManagementRepository) :
    BaseUseCaseParamsResult<AddLogUseCase.Params, Boolean>() {

    override suspend fun run(params: Params): Boolean {
        val logExist = repository.getLogByDate(params.log.date.time) != null
        return if (logExist) {
            repository.updateDailyLog(params.log)
        } else {
            repository.addDailyLog(params.userId, params.log)
        }
    }

    data class Params(val userId: String, val log: DailyLogBO)

}
