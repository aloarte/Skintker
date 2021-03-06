package com.p4r4d0x.domain.repository

import com.p4r4d0x.domain.bo.DailyLogBO


interface LogsManagementRepository {

    suspend fun addDailyLog(userId: String, log: DailyLogBO): Boolean

    suspend fun addAllLogs(logs: List<DailyLogBO>): Boolean

    suspend fun updateDailyLog(log: DailyLogBO): Boolean

    suspend fun getAllLogs(): List<DailyLogBO>

    suspend fun getAllLogsWithFirebase(userId: String): List<DailyLogBO>

    suspend fun getLogByDate(date: Long): DailyLogBO?

    suspend fun getLogsByIrritationLevel(irritationLevel: Int): List<DailyLogBO>

    suspend fun removeAllLogs(userId: String): Boolean
}