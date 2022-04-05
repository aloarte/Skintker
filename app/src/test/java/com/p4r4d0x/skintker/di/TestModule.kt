package com.p4r4d0x.skintker.di

import android.app.Application
import androidx.room.Room
import com.p4r4d0x.skintker.data.datasources.FirebaseLogsManagementDataSource
import com.p4r4d0x.skintker.data.datasources.SurveyDataSource
import com.p4r4d0x.skintker.data.datasources.room.DailyLogDao
import com.p4r4d0x.skintker.data.datasources.room.LogsDatabase
import com.p4r4d0x.skintker.data.repository.LogsManagementRepository
import com.p4r4d0x.skintker.data.repository.SurveyRepository
import com.p4r4d0x.skintker.domain.usecases.*
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import com.p4r4d0x.skintker.presenter.welcome.viewmodel.WelcomeViewModel
import io.mockk.mockk
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testViewModelModule = module {
    viewModel { WelcomeViewModel(get()) }
    viewModel { SurveyViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
}
val testRepositoriesModule = module {
    factory { mockk<SurveyRepository>() }
    factory { mockk<LogsManagementRepository>() }
}

val datasourcesModule = module {
    fun provideDataBase(application: Application): LogsDatabase {
        return Room.databaseBuilder(application, LogsDatabase::class.java, "logs_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(dataBase: LogsDatabase): DailyLogDao {
        return dataBase.dailyLogDao()
    }
    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }
    factory { mockk<SurveyDataSource>() }
    factory { mockk<FirebaseLogsManagementDataSource>() }

}

val testUseCasesModule = module {
    factory { mockk<AddLogUseCase>() }
    factory { mockk<GetLogUseCase>() }
    factory { mockk<GetLogsUseCase>() }
    factory { mockk<GetSurveyUseCase>() }
    factory { mockk<GetQueriedLogsUseCase>() }
    factory { mockk<ExportLogsDBUseCase>() }
}
