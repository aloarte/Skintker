package com.p4r4d0x.skintker.data.repository

import com.p4r4d0x.skintker.data.room.LogsDatabase
import com.p4r4d0x.skintker.domain.bo.DailyLogBO

class LogManagementRepositoryImpl(private val database: LogsDatabase) : LogManagementRepository {
    override suspend fun addDailyLog(log: DailyLogBO): Boolean {
        return database.dailyLogDao().insertDailyLog(log)
    }

    override suspend fun updateDailyLog(log: DailyLogBO): Boolean {
        return database.dailyLogDao().updateDailyLog(log)
    }

    override suspend fun getWeeklyLogs(): List<DailyLogBO> {
        return database.dailyLogDao().getAll().map { dailyLogAndIrritation ->
            dailyLogAndIrritation.toDomain()
        }
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