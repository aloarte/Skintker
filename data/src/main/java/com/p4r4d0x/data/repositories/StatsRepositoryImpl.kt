package com.p4r4d0x.data.repositories

import com.p4r4d0x.data.datasources.StatsDatasource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.repository.StatsRepository

class StatsRepositoryImpl(
    private val dataSource: StatsDatasource
) : StatsRepository {

    override suspend fun getStats(userId: String): PossibleCausesBO? {
        return when (val result = dataSource.getUserStats(userId)) {
            is ApiResult.Success -> {
                result.data
            }
            is ApiResult.Error -> null
        }
    }

}