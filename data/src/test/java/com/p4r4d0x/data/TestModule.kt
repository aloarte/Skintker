package com.p4r4d0x.data

import com.p4r4d0x.data.datasources.FirebaseLogsManagementDataSource
import com.p4r4d0x.data.datasources.SurveyDataSource
import com.p4r4d0x.data.room.LogsDatabase
import com.p4r4d0x.domain.repository.LogsManagementRepository
import com.p4r4d0x.domain.repository.SurveyRepository
import io.mockk.mockk
import org.koin.dsl.module


val testRepositoriesModule = module {
    factory { mockk<SurveyRepository>() }
    factory { mockk<LogsManagementRepository>() }
}

val testDatasourcesModule = module {
    factory { mockk<LogsDatabase>() }
    factory { mockk<SurveyDataSource>() }
    factory { mockk<FirebaseLogsManagementDataSource>() }
}
