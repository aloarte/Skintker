package com.p4r4d0x.skintker.di

import com.p4r4d0x.skintker.data.repository.LogManagementRepository
import com.p4r4d0x.skintker.data.repository.LogManagementRepositoryImpl
import com.p4r4d0x.skintker.data.repository.LogsRepository
import com.p4r4d0x.skintker.domain.usecases.AddLogUseCase
import com.p4r4d0x.skintker.domain.usecases.GetLogsUseCase
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testViewmodelModule = module {
    viewModel { HomeViewModel(get(), get()) }

}
val testRepositoriesModule = module {
    factory { LogsRepository() }
    factory<LogManagementRepository> { LogManagementRepositoryImpl(get()) }
}

val testUseCasesModule = module {
    factory { GetLogsUseCase(get()) }
    factory { AddLogUseCase(get()) }
}
