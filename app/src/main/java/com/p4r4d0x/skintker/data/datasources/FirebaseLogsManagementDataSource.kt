package com.p4r4d0x.skintker.data.datasources

import com.p4r4d0x.skintker.domain.bo.DailyLogBO

interface FirebaseLogsManagementDataSource {

    suspend fun addLog(userId: String, log: DailyLogBO)

    suspend fun getSyncFirebaseLogs(user: String): List<DailyLogBO>

}