package com.p4r4d0x.skintker.domain.usecases

import com.p4r4d0x.skintker.data.FirebaseLogsManagementDataSource
import com.p4r4d0x.skintker.data.repository.LogsManagementRepository
import com.p4r4d0x.skintker.domain.bo.DailyLogBO

class GetLogsUseCase(
    private val logsManagementRepository: LogsManagementRepository,
    private val firebaseDataSource: FirebaseLogsManagementDataSource
) :
    BaseUseCaseParamsResult<GetLogsUseCase.Params, List<DailyLogBO>>() {

    data class Params(val user: String)

    override suspend fun run(params: Params): List<DailyLogBO> {
        val firebaseLogs = firebaseDataSource.getSyncFirebaseLogs(params.user)
        logsManagementRepository.addAllLogs(firebaseLogs)
        return logsManagementRepository.getAllLogs()
    }
}
