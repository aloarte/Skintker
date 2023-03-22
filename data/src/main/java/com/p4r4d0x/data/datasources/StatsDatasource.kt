package com.p4r4d0x.data.datasources

import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.domain.bo.PossibleCausesBO

interface StatsDatasource {

    suspend fun getUserStats(userId: String): ApiResult<PossibleCausesBO?>
}