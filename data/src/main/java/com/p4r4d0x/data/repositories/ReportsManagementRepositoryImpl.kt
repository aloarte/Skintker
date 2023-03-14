package com.p4r4d0x.data.repositories

import com.p4r4d0x.data.datasources.ReportsManagementDataSource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.DailyLogContentsBO
import com.p4r4d0x.domain.bo.ReportStatus
import com.p4r4d0x.domain.repository.ReportsManagementRepository

class ReportsManagementRepositoryImpl(
//    private val database: LogsDatabase,
    private val dataSource: ReportsManagementDataSource
) : ReportsManagementRepository {

    override suspend fun addReport(userId: String, log: DailyLogBO): ReportStatus {
        return when (val result = dataSource.addReport(userId, log)) {
            is ApiResult.Success -> {
//                database.dailyLogDao().insertDailyLog(log)
                result.data
            }
            is ApiResult.Error -> ReportStatus.Error
        }
    }

    override suspend fun getReports(userId: String): DailyLogContentsBO {
        return when (val result = dataSource.getReports(userId)) {
            is ApiResult.Success -> result.data
            is ApiResult.Error -> DailyLogContentsBO()
        }
    }

    override suspend fun getReports(userId: String, count: Int, offset: Int): DailyLogContentsBO {
        return when (val result = dataSource.getReports(userId, count, offset)) {
            is ApiResult.Success -> result.data
            is ApiResult.Error -> DailyLogContentsBO()
        }
    }

    override suspend fun deleteReport(userId: String, logDate: String): Boolean {
        return when (val result = dataSource.deleteReport(userId, logDate)) {
            is ApiResult.Success -> result.data
            is ApiResult.Error -> false
        }
    }

    override suspend fun deleteReports(userId: String): Boolean {
        return when (val result = dataSource.deleteReports(userId)) {
            is ApiResult.Success -> result.data
            is ApiResult.Error -> false
        }
    }


}