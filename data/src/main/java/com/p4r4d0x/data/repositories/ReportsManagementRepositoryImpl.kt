package com.p4r4d0x.data.repositories

import com.p4r4d0x.data.datasources.ReportsManagementDataSource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.data.parsers.DataParser.backendDateToString
import com.p4r4d0x.data.room.DailyLogDao
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.DailyLogContentsBO
import com.p4r4d0x.domain.bo.ReportStatus
import com.p4r4d0x.domain.repository.ReportsManagementRepository
import java.util.*

class ReportsManagementRepositoryImpl(
    private val dao: DailyLogDao,
    private val dataSource: ReportsManagementDataSource
) : ReportsManagementRepository {

    override suspend fun addReport(userId: String, log: DailyLogBO): ReportStatus {
        return when (val result = dataSource.addReport(userId, log)) {
            is ApiResult.Success -> {
                dao.insertDailyLog(log)
                result.data
            }
            is ApiResult.Error -> ReportStatus.Error
        }
    }

    override suspend fun editReport(userId: String, log: DailyLogBO): ReportStatus {
        return when (val result = dataSource.addReport(userId, log)) {
            is ApiResult.Success -> {
                dao.updateDailyLog(log)
                result.data
            }
            is ApiResult.Error -> ReportStatus.Error
        }
    }

    override suspend fun getReport(date: Date): DailyLogBO? =
        dao.loadLogByDate(date.time)?.toDomain()

    override suspend fun getReports(userId: String): DailyLogContentsBO {
        return when (val result = dataSource.getReports(userId)) {
            is ApiResult.Success -> {
                val apiResult = result.data // get data from backend
                dao.insertAllDailyLogs(apiResult.logList) // Insert into the source of truth the data
                //Return with the reports obtained from the database
                DailyLogContentsBO(count = apiResult.count, logList = getReportsFromDatabase())
            }
            is ApiResult.Error -> {
                //If the backend had some error, return the data from database
                with(getReportsFromDatabase()) {
                    DailyLogContentsBO(count = size, logList = this)
                }
            }
        }
    }

    override suspend fun getReports(userId: String, count: Int, offset: Int): DailyLogContentsBO {
        return when (val result = dataSource.getReports(userId, count, offset)) {
            is ApiResult.Success -> {
                with(result.data) {
                    dao.insertAllDailyLogs(logList) // Insert data into the source of truth
                    //Return with the reports obtained from the database
                    DailyLogContentsBO(
                        count = count,
                        logList = getReportsFromDatabase(limit = count, offset = offset)
                    )
                }

            }
            is ApiResult.Error -> {
                //If the backend had some error, return the data from database
                with(getReportsFromDatabase(limit = count, offset = offset)) {
                    DailyLogContentsBO(count = size, logList = this)
                }
            }
        }
    }

    override suspend fun deleteReport(userId: String, date: Date): Boolean {
        return when (val result = dataSource.deleteReport(userId, backendDateToString(date))) {
            is ApiResult.Success -> {
                if (result.data) {
                    try {
                        dao.delete(date.time)
                    } catch (exception: Exception) {
                        return false
                    }
                }
                result.data
            }
            is ApiResult.Error -> false
        }
    }

    override suspend fun deleteReports(userId: String): Boolean {
        return when (val result = dataSource.deleteReports(userId)) {
            is ApiResult.Success -> {
                if (result.data) {
                    try {
                        dao.deleteAll()
                    } catch (exception: Exception) {
                        return false
                    }
                }
                result.data
            }
            is ApiResult.Error -> false
        }
    }

    private suspend fun getReportsFromDatabase() = dao.getAll().map { dailyLogAndIrritation ->
        dailyLogAndIrritation.toDomain()
    }

    private suspend fun getReportsFromDatabase(limit: Int, offset: Int) =
        dao.getLogListPaginated(limit, offset).map { dailyLogAndIrritation ->
            dailyLogAndIrritation.toDomain()
        }

}