package com.p4r4d0x.skintker.domain.usecases

import com.p4r4d0x.skintker.data.FirebaseLogsManagementDataSource
import com.p4r4d0x.skintker.data.repository.LogsManagementRepository
import kotlinx.coroutines.runBlocking

class UpdateDdbbUseCase(
    private val firebaseDataSource: FirebaseLogsManagementDataSource,
    private val logsManagementRepository: LogsManagementRepository,
) : BaseUseCaseNoResult() {
    override suspend fun run() {
        firebaseDataSource.getLogs { logs ->
            runBlocking {
                logsManagementRepository.addAllLogs(logs)
            }
        }
    }

}