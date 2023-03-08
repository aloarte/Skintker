package com.p4r4d0x.skintker.di

import com.p4r4d0x.data.datasources.FirebaseLogsManagementDataSource
import com.p4r4d0x.data.datasources.ResourcesDatasource
import com.p4r4d0x.data.datasources.SurveyDataSource
import com.p4r4d0x.data.datasources.impl.FirebaseLogsManagementDataSourceImpl
import com.p4r4d0x.data.datasources.impl.ResourcesDatasourceImpl
import com.p4r4d0x.data.datasources.impl.SurveyDataSourceImpl
import com.p4r4d0x.data.repositories.LogsManagementRepositoryImpl
import com.p4r4d0x.data.repositories.ResourcesRepositoryImpl
import com.p4r4d0x.data.repositories.SurveyRepositoryImpl
import com.p4r4d0x.domain.repository.LogsManagementRepository
import com.p4r4d0x.domain.repository.ResourcesRepository
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
    factory { GetQueriedLogsUseCase(get()) }
    factory { ExportLogsDBUseCase(get(), get()) }
    factory { RemoveLogsUseCase(get()) }
}

val repositoriesModule = module {
    factory<SurveyRepository> { SurveyRepositoryImpl(get()) }
    factory<ResourcesRepository> { ResourcesRepositoryImpl(get()) }
    factory<LogsManagementRepository> { LogsManagementRepositoryImpl(get(), get()) }
}

val datasourcesModule = module {
    factory<SurveyDataSource> { SurveyDataSourceImpl() }
    factory<ResourcesDatasource> { ResourcesDatasourceImpl() }
    factory<FirebaseLogsManagementDataSource> { FirebaseLogsManagementDataSourceImpl() }

}

