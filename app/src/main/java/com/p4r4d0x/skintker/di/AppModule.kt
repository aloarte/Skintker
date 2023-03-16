package com.p4r4d0x.skintker.di

import com.google.gson.Gson
import com.p4r4d0x.data.datasources.ReportsManagementDataSource
import com.p4r4d0x.data.datasources.ResourcesDatasource
import com.p4r4d0x.data.datasources.StatsDatasource
import com.p4r4d0x.data.datasources.SurveyDataSource
import com.p4r4d0x.data.datasources.impl.ReportsManagementDataSourceImpl
import com.p4r4d0x.data.datasources.impl.ResourcesDatasourceImpl
import com.p4r4d0x.data.datasources.impl.StatsDataSourceImpl
import com.p4r4d0x.data.datasources.impl.SurveyDataSourceImpl
import com.p4r4d0x.data.repositories.ReportsManagementRepositoryImpl
import com.p4r4d0x.data.repositories.ResourcesRepositoryImpl
import com.p4r4d0x.data.repositories.StatsRepositoryImpl
import com.p4r4d0x.data.repositories.SurveyRepositoryImpl
import com.p4r4d0x.domain.repository.ReportsManagementRepository
import com.p4r4d0x.domain.repository.ResourcesRepository
import com.p4r4d0x.domain.repository.StatsRepository
import com.p4r4d0x.domain.repository.SurveyRepository
import com.p4r4d0x.domain.usecases.*
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import com.p4r4d0x.skintker.presenter.login.viewmodel.LoginViewModel
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import com.p4r4d0x.skintker.presenter.welcome.viewmodel.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel { WelcomeViewModel(get()) }
    viewModel { LoginViewModel() }
    viewModel { SurveyViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
}

val useCasesModule = module {
    factory { AddLogUseCase(get()) }
    factory { GetLogUseCase(get()) }
    factory { GetLogsUseCase(get()) }
    factory { GetSurveyUseCase(get()) }
    factory { GetStatsUseCase(get()) }
    factory { ExportLogsDBUseCase(get(), get()) }
    factory { RemoveLogsUseCase(get()) }
}

val repositoriesModule = module {
    factory<SurveyRepository> { SurveyRepositoryImpl(get()) }
    factory<ResourcesRepository> { ResourcesRepositoryImpl(get()) }
    factory<StatsRepository> { StatsRepositoryImpl(get()) }
    factory<ReportsManagementRepository> { ReportsManagementRepositoryImpl(get(), get()) }

}

val datasourcesModule = module {
    factory<SurveyDataSource> { SurveyDataSourceImpl() }
    factory<ResourcesDatasource> { ResourcesDatasourceImpl() }
    factory<StatsDatasource> { StatsDataSourceImpl(get(), get()) }
    factory<ReportsManagementDataSource> { ReportsManagementDataSourceImpl(get(), get()) }

}

val externalModule = module {
    factory { Gson() }
}
