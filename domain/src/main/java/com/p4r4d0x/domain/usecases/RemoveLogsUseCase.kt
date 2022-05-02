package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.repository.LogsManagementRepository

class RemoveLogsUseCase(private val repository: LogsManagementRepository) :
    BaseUseCaseParamsResult<RemoveLogsUseCase.Params, Boolean>() {

    override suspend fun run(params: Params): Boolean {
        return repository.removeAllLogs(params.userId)
    }

    data class Params(val userId: String)

}
