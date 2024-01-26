package com.p4r4d0x.data.datasources.impl

import com.p4r4d0x.data.Constants
import com.p4r4d0x.data.Constants.JSON_PARSE_EMPTY_BODY
import com.p4r4d0x.data.Constants.JSON_PARSE_EMPTY_BODY_CODE
import com.p4r4d0x.data.Constants.JSON_PARSE_EXCEPTION_CODE
import com.p4r4d0x.data.Constants.JSON_PARSE_NO_BODY
import com.p4r4d0x.data.Constants.JSON_PARSE_NO_BODY_CODE
import com.p4r4d0x.data.api.SkintkvaultApi
import com.p4r4d0x.data.datasources.ReportsManagementDataSource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.data.dto.SkintkvaultResponseLogs
import com.p4r4d0x.data.dto.logs.toDto
import com.p4r4d0x.data.getInsertReportStatus
import com.p4r4d0x.data.parsers.DataParser.toDailyLogContents
import com.p4r4d0x.data.wasInsertSuccessful
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.DailyLogContentsBO
import com.p4r4d0x.domain.bo.ReportStatus
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody

class ReportsManagementDataSourceImpl(
    private val api: SkintkvaultApi,
    private val moshi: Moshi
) : ReportsManagementDataSource {

    private fun parseSkintkvaultResponse(body: ResponseBody?): SkintkvaultResponseLogs = try {

        body?.string()?.let {
            if (it.isEmpty()) SkintkvaultResponseLogs(
                statusCode = JSON_PARSE_EMPTY_BODY_CODE,
                statusMessage = JSON_PARSE_EMPTY_BODY
            )
            else moshi.adapter(SkintkvaultResponseLogs::class.java).fromJson(it)

        } ?: SkintkvaultResponseLogs(
            statusCode = JSON_PARSE_NO_BODY_CODE,
            statusMessage = JSON_PARSE_NO_BODY
        )
    } catch (e: Exception) {
        SkintkvaultResponseLogs(statusCode = JSON_PARSE_EXCEPTION_CODE, statusMessage = e.message)
    }

    override suspend fun addReport(userId: String, log: DailyLogBO): ApiResult<ReportStatus> {
        return try {
            val response = api.addReport(userId, log.toDto())
            if (response.code().wasInsertSuccessful()) {
                with(parseSkintkvaultResponse(response.body())) {
                    if (statusCode >= 0) ApiResult.Success(response.code().getInsertReportStatus())
                    else ApiResult.Error(statusCode, statusMessage)
                }

            } else {
                ApiResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.Error(
                errorCode = Constants.API_CALL_EXCEPTION_CODE,
                errorMessage = Constants.API_CALL_EXCEPTION_MESSAGE,
                exception = e
            )
        }
    }

    override suspend fun getReports(userId: String): ApiResult<DailyLogContentsBO> {
        return try {
            val response = api.getReports(userId)
            if (response.code() == Constants.API_SUCCESS_CODE) {
                with(parseSkintkvaultResponse(response.body())) {
                    if (statusCode >= 0) ApiResult.Success(this.toDailyLogContents())
                    else ApiResult.Error(statusCode, statusMessage)
                }
            } else {
                ApiResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.Error(
                errorCode = Constants.API_CALL_EXCEPTION_CODE,
                errorMessage = Constants.API_CALL_EXCEPTION_MESSAGE,
                exception = e
            )
        }
    }

    override suspend fun getReports(
        userId: String,
        count: Int,
        offset: Int
    ): ApiResult<DailyLogContentsBO> {
        return try {
            val response = api.getReportsPaginated(userId, count, offset)
            if (response.code() == Constants.API_SUCCESS_CODE) {
                with(parseSkintkvaultResponse(response.body())) {
                    if (statusCode >= 0) ApiResult.Success(this.toDailyLogContents())
                    else ApiResult.Error(statusCode, statusMessage)
                }
            } else {
                ApiResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.Error(
                errorCode = Constants.API_CALL_EXCEPTION_CODE,
                errorMessage = Constants.API_CALL_EXCEPTION_MESSAGE,
                exception = e
            )
        }
    }

    override suspend fun deleteReport(userId: String, logDate: String): ApiResult<Boolean> {
        return try {
            val response = api.deleteReport(userId, logDate)
            if (response.code() == Constants.API_SUCCESS_CODE) {
                ApiResult.Success(true)
            } else {
                ApiResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.Error(
                errorCode = Constants.API_CALL_EXCEPTION_CODE,
                errorMessage = Constants.API_CALL_EXCEPTION_MESSAGE,
                exception = e
            )
        }
    }

    override suspend fun deleteReports(userId: String): ApiResult<Boolean> {
        return try {
            val response = api.deleteAllReports(userId)
            if (response.code() == Constants.API_SUCCESS_CODE) {
                ApiResult.Success(true)
            } else {
                ApiResult.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.Error(
                errorCode = Constants.API_CALL_EXCEPTION_CODE,
                errorMessage = Constants.API_CALL_EXCEPTION_MESSAGE,
                exception = e
            )
        }
    }

}