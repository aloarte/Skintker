package com.p4r4d0x.skintker.di

import com.p4r4d0x.skintker.presenter.view.LogsRepository
import com.p4r4d0x.skintker.presenter.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel { MainViewModel() }

}
val repositoriesModule = module {
    factory { LogsRepository() }
}