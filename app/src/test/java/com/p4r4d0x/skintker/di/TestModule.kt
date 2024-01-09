package com.p4r4d0x.skintker.di

import com.p4r4d0x.domain.usecases.*
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
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get(),get()) }
}

val testUseCasesModule = module {
    factory { mockk<AddLogUseCase>() }
    factory { mockk<GetLogUseCase>() }
    factory { mockk<GetLogsUseCase>() }
    factory { mockk<GetSurveyUseCase>() }
    factory { mockk<GetStatsUseCase>() }
    factory { mockk<ExportLogsDBUseCase>() }
    factory { mockk<RemoveLogsUseCase>() }
    factory { mockk<RemoveLocalLogsUseCase>() }
    factory { mockk<RemoveLogUseCase>() }

}