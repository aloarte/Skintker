package com.p4r4d0x.data.datasources.impl

import com.p4r4d0x.data.Constants
import com.p4r4d0x.data.api.SkintkvaultApi
import com.p4r4d0x.data.datasources.ReportsManagementDataSource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.data.getInsertReportStatus
import com.p4r4d0x.data.parsers.toDailyLogContents
import com.p4r4d0x.data.parsers.toDto
import com.p4r4d0x.data.wasInsertSuccessful
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.DailyLogContentsBO
import com.p4r4d0x.domain.bo.ReportStatus

class ReportsManagementDataSourceImpl(
    private val api: SkintkvaultApi
) : ReportsManagementDataSource {

    override suspend fun addReport(userId: String, log: DailyLogBO): ApiResult<ReportStatus> {
        return try {
            val response = api.addReport(userId, log.toDto())
            if (response.statusCode.wasInsertSuccessful()) {
                ApiResult.Success(response.statusCode.getInsertReportStatus())
            } else {
                ApiResult.Error(response.statusCode, response.statusMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error(Constants.API_CALL_EXCEPTION, Constants.API_CALL_EXCEPTION_MESSAGE, e)
        }
    }

    override suspend fun getReports(userId: String): ApiResult<DailyLogContentsBO> {
        return try {
            val response = api.getReports(userId)
            if (response.statusCode == Constants.API_SUCCESS) {
                ApiResult.Success(response.toDailyLogContents())
            } else {
                ApiResult.Error(response.statusCode, response.statusMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error(Constants.API_CALL_EXCEPTION, Constants.API_CALL_EXCEPTION_MESSAGE, e)
        }
    }

    override suspend fun getReports(
        userId: String,
        count: Int,
        offset: Int
    ): ApiResult<DailyLogContentsBO> {
        return try {
            val response = api.getReportsPaginated(userId, count, offset)
            if (response.statusCode == Constants.API_SUCCESS) {
                ApiResult.Success(response.toDailyLogContents())
            } else {
                ApiResult.Error(response.statusCode, response.statusMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error(Constants.API_CALL_EXCEPTION, Constants.API_CALL_EXCEPTION_MESSAGE, e)
        }
    }

    override suspend fun deleteReport(userId: String, logDate: String): ApiResult<Boolean> {
        return try {
            val response = api.deleteReport(userId, logDate)
            if (response.statusCode == Constants.API_SUCCESS) {
                ApiResult.Success(true)
            } else {
                ApiResult.Error(response.statusCode, response.statusMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error(Constants.API_CALL_EXCEPTION, Constants.API_CALL_EXCEPTION_MESSAGE, e)
        }
    }

    override suspend fun deleteReports(userId: String): ApiResult<Boolean> {
        return try {
            val response = api.deleteAllReports(userId)
            if (response.statusCode == Constants.API_SUCCESS) {
                ApiResult.Success(true)
            } else {
                ApiResult.Error(response.statusCode, response.statusMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error(Constants.API_CALL_EXCEPTION, Constants.API_CALL_EXCEPTION_MESSAGE, e)
        }
    }

}