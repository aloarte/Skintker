package com.p4r4d0x.skintker.di

import com.p4r4d0x.skintker.data.datasources.FirebaseLogsManagementDataSource
import com.p4r4d0x.skintker.data.datasources.SurveyDataSource
import com.p4r4d0x.skintker.data.datasources.room.LogsDatabase
import com.p4r4d0x.skintker.data.repository.LogsManagementRepository
import com.p4r4d0x.skintker.data.repository.SurveyRepository
import com.p4r4d0x.skintker.domain.usecases.*
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import com.p4r4d0x.skintker.presenter.welcome.viewmodel.WelcomeViewModel
import io.mockk.mockk
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

val testDatasourcesModule = module {
    factory { mockk<LogsDatabase>() }
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