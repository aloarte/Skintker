package com.p4r4d0x.skintker.data.repository

import com.p4r4d0x.skintker.data.FirebaseLogsManagementDataSource
import com.p4r4d0x.skintker.data.room.LogsDatabase
import com.p4r4d0x.skintker.domain.bo.DailyLogBO

class LogsManagementRepositoryImpl(
    private val database: LogsDatabase,
    private val firebaseDatabase: FirebaseLogsManagementDataSource
) : LogsManagementRepository {


    override suspend fun addDailyLog(log: DailyLogBO): Boolean {
        firebaseDatabase.addLog(log)
        return database.dailyLogDao().insertDailyLog(log)
    }

    override suspend fun addAllLogs(logs: List<DailyLogBO>): Boolean {
        return database.dailyLogDao().insertAllDailyLogs(logs)
    }

    override suspend fun updateDailyLog(log: DailyLogBO): Boolean {
        return database.dailyLogDao().updateDailyLog(log)
    }

    override suspend fun getAllLogs(): List<DailyLogBO> {
        return database.dailyLogDao().getAll().map { dailyLogAndIrritation ->
            dailyLogAndIrritation.toDomain()
        }
    }

    override suspend fun getLogByDate(date: Long): DailyLogBO? {
        return database.dailyLogDao().loadLogByDate(date)?.toDomain()
    }

    override suspend fun getLogsByIrritationLevel(irritationLevel: Int): List<DailyLogBO> {
        return database.dailyLogDao().getLogsWithIrritationLevel(irritationLevel)
            .map { it.toDomain() }
    }

}