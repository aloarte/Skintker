package com.p4r4d0x.domain.repository

import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.DailyLogContentsBO
import com.p4r4d0x.domain.bo.ReportStatus
import java.util.*

interface ReportsManagementRepository {

    suspend fun addReport(userId: String, log: DailyLogBO): ReportStatus

    suspend fun editReport(userId: String, log: DailyLogBO): ReportStatus

    suspend fun getReport(date: Date): DailyLogBO?

    suspend fun getReports(userId: String): DailyLogContentsBO

    suspend fun getReports(userId: String, count: Int, offset: Int): DailyLogContentsBO

    suspend fun deleteReport(userId: String, date: Date): Boolean

    suspend fun deleteReports(userId: String): Boolean
}