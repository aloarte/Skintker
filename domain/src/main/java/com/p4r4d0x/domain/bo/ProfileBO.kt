package com.p4r4d0x.domain.bo

sealed class ProfileBO(val isAnonymous: Boolean) {
    data class AuthenticatedProfileBO(val email: String, val name: String, val accountId: String) :
        ProfileBO(false)

    class AnonymousProfileBO : ProfileBO(true)

}