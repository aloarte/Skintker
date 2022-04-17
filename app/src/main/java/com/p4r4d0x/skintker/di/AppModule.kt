package com.p4r4d0x.skintker.di

import android.app.Application
import androidx.room.Room
import com.p4r4d0x.data.datasources.FirebaseLogsManagementDataSource
import com.p4r4d0x.data.datasources.impl.FirebaseLogsManagementDataSourceImpl
import com.p4r4d0x.data.datasources.SurveyDataSource
import com.p4r4d0x.data.datasources.impl.SurveyDataSourceImpl
import com.p4r4d0x.data.room.DailyLogDao
import com.p4r4d0x.data.room.LogsDatabase
import com.p4r4d0x.data.repositories.LogsManagementRepositoryImpl
import com.p4r4d0x.domain.repository.LogsManagementRepository
import com.p4r4d0x.domain.repository.SurveyRepository
import com.p4r4d0x.domain.usecases.*
import com.p4r4d0x.data.repositories.SurveyRepositoryImpl
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import com.p4r4d0x.skintker.presenter.login.viewmodel.LoginViewModel
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import com.p4r4d0x.skintker.presenter.welcome.viewmodel.WelcomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel { WelcomeViewModel(get()) }
    viewModel { LoginViewModel() }
    viewModel { SurveyViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
}
val repositoriesModule = module {
    factory<SurveyRepository> { SurveyRepositoryImpl(get()) }
    factory<LogsManagementRepository> {
        LogsManagementRepositoryImpl(
            get(),
            get()
        )
    }
}

val useCasesModule = module {
    factory { AddLogUseCase(get()) }
    factory { GetLogUseCase(get()) }
    factory { GetLogsUseCase(get()) }
    factory { GetSurveyUseCase(get()) }
    factory { GetQueriedLogsUseCase(get()) }
    factory { ExportLogsDBUseCase(get(),get()) }
}

val dataSourcesModule = module {
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
    factory<SurveyDataSource> { SurveyDataSourceImpl() }
    factory<FirebaseLogsManagementDataSource> { FirebaseLogsManagementDataSourceImpl() }

}