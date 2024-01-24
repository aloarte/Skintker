package com.p4r4d0x.data.datasources

import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.data.dto.user.UserResultEnum

interface UserDataSource {

    suspend fun loginUser(userId: String): ApiResult<UserResultEnum>
}