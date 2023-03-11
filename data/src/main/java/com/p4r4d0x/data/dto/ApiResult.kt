package com.p4r4d0x.data.dto

sealed class ApiResult<T> {

    class Success<T>(val data: T) : ApiResult<T>()

    class Error<T>(
        val errorCode: Int,
        val errorMessage: String?,
        val exception: Exception? = null
    ) : ApiResult<T>()
}
