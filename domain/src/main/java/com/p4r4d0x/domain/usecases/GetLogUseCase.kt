package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.repository.LogsManagementRepository
import java.util.*

class GetLogUseCase(private val repository: LogsManagementRepository) :
    BaseUseCaseParamsResult<GetLogUseCase.Params, DailyLogBO?>() {

    override suspend fun run(params: Params): DailyLogBO? {
        return repository.getLogByDate(params.date.time)
    }

    data class Params(val date: Date)
}