package com.p4r4d0x.data.datasources

import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.DailyLogContentsBO
import com.p4r4d0x.domain.bo.ReportStatus

interface ReportsManagementDataSource {

    suspend fun addReport(userId: String, log: DailyLogBO): ApiResult<ReportStatus>

    suspend fun getReports(userId: String): ApiResult<DailyLogContentsBO>

    suspend fun getReports(userId: String, count: Int, offset: Int): ApiResult<DailyLogContentsBO>

    suspend fun deleteReport(userId: String, logDate: String): ApiResult<Boolean>

    suspend fun deleteReports(userId: String): ApiResult<Boolean>

}