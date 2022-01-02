package com.p4r4d0x.skintker.domain.usecases

import com.p4r4d0x.skintker.data.repository.LogManagementRepository
import com.p4r4d0x.skintker.domain.BaseUseCaseParamsResult
import com.p4r4d0x.skintker.domain.bo.DailyLogBO

class AddLogUseCase(private val repository: LogManagementRepository) :
    BaseUseCaseParamsResult<AddLogUseCase.Params, Boolean>() {

    override suspend fun run(params: Params): Boolean {
        val logExist = repository.getLogByDate(params.log.date.time) != null
        return if (logExist) {
//            repository.addDailyLog(params.log)
            repository.updateDailyLog(params.log)
        } else {
            repository.addDailyLog(params.log)
        }
    }

    data class Params(val log: DailyLogBO)

}