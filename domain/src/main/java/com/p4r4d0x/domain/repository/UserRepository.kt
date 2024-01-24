package com.p4r4d0x.domain.repository

interface UserRepository {
    suspend fun loginUser(userId: String): Boolean
}