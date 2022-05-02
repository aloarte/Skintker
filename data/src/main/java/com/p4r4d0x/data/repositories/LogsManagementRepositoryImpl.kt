package com.p4r4d0x.data.repositories

import com.p4r4d0x.data.datasources.FirebaseLogsManagementDataSource
import com.p4r4d0x.data.room.LogsDatabase
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.repository.LogsManagementRepository

class LogsManagementRepositoryImpl(
    private val database: LogsDatabase,
    private val firebaseDatabaseImpl: FirebaseLogsManagementDataSource
) : LogsManagementRepository {

    override suspend fun addDailyLog(userId: String, log: DailyLogBO): Boolean {
        firebaseDatabaseImpl.addSyncLog(userId, log)
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

    override suspend fun getAllLogsWithFirebase(userId: String): List<DailyLogBO> {
        val firebaseLogs = firebaseDatabaseImpl.getSyncFirebaseLogs(userId)
        addAllLogs(firebaseLogs)
        return getAllLogs()
    }

    override suspend fun getLogByDate(date: Long): DailyLogBO? {
        return database.dailyLogDao().loadLogByDate(date)?.toDomain()
    }

    override suspend fun getLogsByIrritationLevel(irritationLevel: Int): List<DailyLogBO> {
        return database.dailyLogDao().getLogsWithIrritationLevel(irritationLevel)
            .map { it.toDomain() }
    }

    override suspend fun removeAllLogs(userId: String): Boolean {
        val firebaseRemoved = firebaseDatabaseImpl.removeSyncLogs(userId)
        database.dailyLogDao().deleteAllLogs()
        return firebaseRemoved
    }

}