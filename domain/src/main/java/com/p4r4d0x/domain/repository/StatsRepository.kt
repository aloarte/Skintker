package com.p4r4d0x.domain.repository

import com.p4r4d0x.domain.bo.PossibleCausesBO

interface StatsRepository {

    suspend fun getStats(userId: String): PossibleCausesBO?
}