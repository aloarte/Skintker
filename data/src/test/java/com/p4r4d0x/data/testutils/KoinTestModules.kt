package com.p4r4d0x.data.testutils

import android.content.res.Resources
import com.p4r4d0x.data.api.SkintkvaultApi
import com.p4r4d0x.data.datasources.ReportsManagementDataSource
import com.p4r4d0x.data.datasources.StatsDatasource
import com.p4r4d0x.data.datasources.SurveyDataSource
import com.p4r4d0x.data.datasources.UserDataSource
import com.p4r4d0x.data.parsers.LogsNormalizer
import com.p4r4d0x.data.room.DailyLogDao
import com.p4r4d0x.data.room.LogsDatabase
import com.p4r4d0x.domain.repository.ReportsManagementRepository
import com.p4r4d0x.domain.repository.StatsRepository
import com.p4r4d0x.domain.repository.SurveyRepository
import com.p4r4d0x.domain.repository.UserRepository
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module

val appContextModule = module {
    factory { mockk<Resources>() }
    factory { mockk<LogsNormalizer>() }
}

val apiModule = module {
    factory { mockk<SkintkvaultApi>() }
    factory { "application/json; charset=utf-8".toMediaType() }
}

val databaseModule = module {
    factory { mockk<DailyLogDao>() }
}

val testRepositoriesModule = module {
    factory { mockk<StatsRepository>() }
    factory { mockk<SurveyRepository>() }
    factory { mockk<ReportsManagementRepository>() }
    factory { mockk<UserRepository>() }
}

val testDatasourcesModule = module {
    factory { mockk<LogsDatabase>() }
    factory { mockk<StatsDatasource>() }
    factory { mockk<SurveyDataSource>() }
    factory { mockk<ReportsManagementDataSource>() }
    factory { mockk<UserDataSource>() }
}
