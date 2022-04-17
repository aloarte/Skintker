package com.p4r4d0x.data.datasources

import com.p4r4d0x.domain.bo.DailyLogBO

interface FirebaseLogsManagementDataSource {

    suspend fun addSyncLog(userId: String, log: DailyLogBO): Boolean

    suspend fun getSyncFirebaseLogs(user: String): List<DailyLogBO>

}