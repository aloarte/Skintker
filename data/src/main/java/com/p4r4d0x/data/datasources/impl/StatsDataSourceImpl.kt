package com.p4r4d0x.data.datasources.impl

import com.p4r4d0x.data.Constants
import com.p4r4d0x.data.Constants.JSON_PARSE_EMPTY_BODY
import com.p4r4d0x.data.Constants.JSON_PARSE_EMPTY_BODY_CODE
import com.p4r4d0x.data.Constants.JSON_PARSE_EMPTY_STATS
import com.p4r4d0x.data.Constants.JSON_PARSE_EXCEPTION_CODE
import com.p4r4d0x.data.Constants.JSON_PARSE_NO_BODY
import com.p4r4d0x.data.Constants.JSON_PARSE_NO_BODY_CODE
import com.p4r4d0x.data.api.SkintkvaultApi
import com.p4r4d0x.data.datasources.StatsDatasource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.data.dto.SkintkvaultResponseStats
import com.p4r4d0x.data.parsers.DataParser.toPossibleCauses
import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody

class StatsDataSourceImpl(
    private val api: SkintkvaultApi,
    private val moshi: Moshi
) : StatsDatasource {

    private fun parseSkintkvaultResponse(body: ResponseBody?): SkintkvaultResponseStats = try {
        body?.string()?.let {
            if (it.isEmpty()) SkintkvaultResponseStats(
                statusCode = JSON_PARSE_EMPTY_BODY_CODE,
                statusMessage = JSON_PARSE_EMPTY_BODY
            )
            else moshi.adapter(SkintkvaultResponseStats::class.java).fromJson(it)

        } ?: SkintkvaultResponseStats(
            statusCode = JSON_PARSE_NO_BODY_CODE,
            statusMessage = JSON_PARSE_NO_BODY
        )
    } catch (e: Exception) {
        SkintkvaultResponseStats(statusCode = JSON_PARSE_EXCEPTION_CODE, statusMessage = e.message)
    }

    override suspend fun getUserStats(userId: String): ApiResult<PossibleCausesBO?> {
        return try {
            val response = api.getStats(userId)
            if (response.code() == Constants.API_SUCCESS_CODE) {
                with(parseSkintkvaultResponse(response.body())) {
                    if (statusCode >= 0) {
                        this.toPossibleCauses()?.let {
                            ApiResult.Success(it)
                        } ?: ApiResult.Error(statusCode, JSON_PARSE_EMPTY_STATS)
                    } else ApiResult.Error(statusCode, statusMessage)
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

}
