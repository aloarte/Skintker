package com.p4r4d0x.skintker.data.datasources

import com.p4r4d0x.skintker.domain.bo.DailyLogBO

interface FirebaseLogsManagementDataSource {

    suspend fun addSyncLog(userId: String, log: DailyLogBO): Boolean

    suspend fun getSyncFirebaseLogs(user: String): List<DailyLogBO>

}