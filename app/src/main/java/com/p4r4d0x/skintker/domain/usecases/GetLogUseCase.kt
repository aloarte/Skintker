package com.p4r4d0x.skintker.domain.usecases

import com.p4r4d0x.skintker.data.repository.LogManagementRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import java.util.*

class GetLogUseCase(private val repository: LogManagementRepository) :
    BaseUseCaseParamsResult<GetLogUseCase.Params, DailyLogBO?>() {

    override suspend fun run(params: Params): DailyLogBO? {
        val log = repository.getLogByDate(params.date.time)
        return log
    }

    data class Params(val date: Date)
}