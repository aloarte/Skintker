package com.p4r4d0x.skintker.data.repository

import com.p4r4d0x.skintker.data.room.LogsDatabase
import com.p4r4d0x.skintker.domain.bo.DailyLogBO

class LogManagementRepositoryImpl(private val database: LogsDatabase) : LogManagementRepository {
    override suspend fun addDailyLog(log: DailyLogBO): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateRecord(log: DailyLogBO): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getWeeklyLogs(): List<DailyLogBO> {
        return database.dailyLogDao().getAll().map { dailyLogAndIrritation ->
            dailyLogAndIrritation.toDomain()
        }
    }

    override suspend fun getAllLogs(): List<DailyLogBO> {
        TODO("Not yet implemented")
    }
}