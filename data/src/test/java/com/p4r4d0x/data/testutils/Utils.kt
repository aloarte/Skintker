package com.p4r4d0x.data.testutils

import okhttp3.MediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

object Utils {
    fun MediaType.buildResponse(resultCode: Int, json: String = "{}"): Response<ResponseBody> {
        val responseBody = json.toResponseBody(this)

        return if (resultCode in 200..299) {
            Response.success(resultCode, responseBody)
        } else {
            Response.error(resultCode, responseBody)
        }
    }
}