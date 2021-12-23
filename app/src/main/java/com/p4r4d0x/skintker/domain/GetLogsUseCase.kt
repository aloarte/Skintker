package com.p4r4d0x.skintker.domain

import com.p4r4d0x.skintker.data.repository.LogManagementRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO

class GetLogsUseCase(private val repository: LogManagementRepository) :
    BaseUseCase<List<DailyLogBO>>() {


    override suspend fun run(): List<DailyLogBO> {
        return repository.getAllLogs()
    }

}