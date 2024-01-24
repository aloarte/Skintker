package com.p4r4d0x.skintker.presenter.login

data class LoginLoadingState constructor(
    val status: LoginStatus,
    val message: String? = null
) {

    companion object {
        val LOADING = LoginLoadingState(LoginStatus.Running)
        val LOADED = LoginLoadingState(LoginStatus.Success)
        fun error(message: String?) = LoginLoadingState(LoginStatus.Failed, message)
    }

    enum class LoginStatus {
        Running,
        Success,
        Failed,
        Idle,
        LoggedIn
    }
}