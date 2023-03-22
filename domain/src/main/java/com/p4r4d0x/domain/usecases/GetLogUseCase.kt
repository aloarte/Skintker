package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.repository.ReportsManagementRepository
import java.util.*

class GetLogUseCase(
    private val reportsMgmtRepository: ReportsManagementRepository
) : BaseUseCaseParamsResult<GetLogUseCase.Params, DailyLogBO?>() {

    override suspend fun run(params: Params): DailyLogBO? {
        return reportsMgmtRepository.getReport(params.date)
    }

    data class Params(val date: Date)
}