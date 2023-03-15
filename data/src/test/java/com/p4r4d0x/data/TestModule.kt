package com.p4r4d0x.data

import com.p4r4d0x.data.api.SkintkvaultApi
import com.p4r4d0x.data.datasources.FirebaseLogsManagementDataSource
import com.p4r4d0x.data.datasources.ReportsManagementDataSource
import com.p4r4d0x.data.datasources.SurveyDataSource
import com.p4r4d0x.data.room.DailyLogDao
import com.p4r4d0x.data.room.LogsDatabase
import com.p4r4d0x.domain.repository.LogsManagementRepository
import com.p4r4d0x.domain.repository.ReportsManagementRepository
import com.p4r4d0x.domain.repository.SurveyRepository
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module

val apiModule = module {
    factory { mockk<SkintkvaultApi>() }
    factory { "application/json; charset=utf-8".toMediaType() }
}

val databaseModule = module {
    factory { mockk<DailyLogDao>() }
}

val testRepositoriesModule = module {
    factory { mockk<SurveyRepository>() }
    factory { mockk<LogsManagementRepository>() }
    factory { mockk<ReportsManagementRepository>() }

}

val testDatasourcesModule = module {
    factory { mockk<LogsDatabase>() }
    factory { mockk<SurveyDataSource>() }
    factory { mockk<FirebaseLogsManagementDataSource>() }
    factory { mockk<ReportsManagementDataSource>() }


}
