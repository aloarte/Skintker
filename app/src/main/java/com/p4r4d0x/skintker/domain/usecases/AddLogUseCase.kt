package com.p4r4d0x.skintker.domain.usecases

import com.p4r4d0x.skintker.data.repository.LogsManagementRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO

class AddLogUseCase(private val repository: LogsManagementRepository) :
    BaseUseCaseParamsResult<AddLogUseCase.Params, Boolean>() {

    override suspend fun run(params: Params): Boolean {
        val logExist = repository.getLogByDate(params.log.date.time) != null
        return if (logExist) {
            repository.updateDailyLog(params.log)
        } else {
            repository.addDailyLog(params.log)
        }
    }

    data class Params(val log: DailyLogBO)

}
