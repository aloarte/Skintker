package com.p4r4d0x.data.datasources.impl

import com.p4r4d0x.data.Constants
import com.p4r4d0x.data.api.SkintkvaultApi
import com.p4r4d0x.data.datasources.UserDataSource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.data.dto.SkintkvaultResponseUser
import com.p4r4d0x.data.dto.user.UserDto
import com.p4r4d0x.data.dto.user.UserResultEnum
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody

class UserDataSourceImpl(
    private val api: SkintkvaultApi,
    private val moshi: Moshi
) : UserDataSource {

    override suspend fun loginUser(userId: String): ApiResult<UserResultEnum> {
        return try {
            val response = api.addUser(UserDto(userId))
            if (response.code() == Constants.API_SUCCESS_CODE) {
                val sktResponse = parseSkintkvaultResponse(response.body())

                ApiResult.Success(sktResponse.result?:UserResultEnum.Unknown)
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

    private fun parseSkintkvaultResponse(body: ResponseBody?): SkintkvaultResponseUser = try {

        body?.string()?.let {
            if (it.isEmpty()) SkintkvaultResponseUser(
                statusCode = Constants.JSON_PARSE_EMPTY_BODY_CODE
            )
            else moshi.adapter(SkintkvaultResponseUser::class.java).fromJson(it)

        } ?: SkintkvaultResponseUser(
            statusCode = Constants.JSON_PARSE_NO_BODY_CODE
        )
    } catch (e: Exception) {
        SkintkvaultResponseUser(
            statusCode = Constants.JSON_PARSE_EXCEPTION_CODE
        )
    }

}