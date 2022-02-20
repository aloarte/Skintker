package com.p4r4d0x.skintker.domain.usecases

import com.p4r4d0x.skintker.data.FirebaseLogsManagementDataSource
import com.p4r4d0x.skintker.data.repository.LogsManagementRepository
import kotlinx.coroutines.runBlocking

class UpdateLogsUseCase(
    private val firebaseDataSource: FirebaseLogsManagementDataSource,
    private val logsManagementRepository: LogsManagementRepository,
) : BaseUseCaseParamsNoResult<UpdateLogsUseCase.Params>() {

    override suspend fun run(params: Params) {
        firebaseDataSource.getLogs(params.user) { logs ->
            runBlocking {
                logsManagementRepository.addAllLogs(logs)
                params.onLogsUpdated.invoke()
            }
        }
    }

    data class Params(val user: String, val onLogsUpdated: () -> Unit)

}