package com.p4r4d0x.skintker.data.repository

import com.p4r4d0x.skintker.domain.bo.DailyLogBO


interface LogManagementRepository {

    suspend fun addDailyLog(log: DailyLogBO): Boolean

    suspend fun updateDailyLog(log: DailyLogBO): Boolean

    suspend fun getWeeklyLogs(): List<DailyLogBO>

    suspend fun getAllLogs(): List<DailyLogBO>

    suspend fun getLogByDate(date: Long): DailyLogBO?

    suspend fun getLogsByIrritationLevel(irritationLevel: Int): List<DailyLogBO>
}