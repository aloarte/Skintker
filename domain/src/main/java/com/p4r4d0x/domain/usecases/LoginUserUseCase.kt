package com.p4r4d0x.domain.usecases

import com.p4r4d0x.domain.repository.UserRepository

class LoginUserUseCase(
    private val userRepository: UserRepository
) : BaseUseCaseParamsResult<LoginUserUseCase.Params, Boolean>() {

    override suspend fun run(params: Params): Boolean {
        return userRepository.loginUser(params.userId)
    }

    data class Params(val userId: String)

}
