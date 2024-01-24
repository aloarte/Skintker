package com.p4r4d0x.data.repositories

import com.p4r4d0x.data.datasources.UserDataSource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.data.dto.user.UserResultEnum
import com.p4r4d0x.domain.repository.UserRepository

class UserRepositoryImpl(
    private val dataSource: UserDataSource
) : UserRepository {

    override suspend fun loginUser(userId: String): Boolean {
        return when (val result = dataSource.loginUser(userId)) {
            is ApiResult.Success -> {
                result.data == UserResultEnum.UserInserted || result.data == UserResultEnum.UserExist
            }
            is ApiResult.Error -> false
        }
    }

}