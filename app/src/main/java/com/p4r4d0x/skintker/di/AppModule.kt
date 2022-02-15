package com.p4r4d0x.skintker.di

import android.app.Application
import androidx.room.Room
import com.p4r4d0x.skintker.data.FirebaseLogsManagementDataSource
import com.p4r4d0x.skintker.data.repository.LogsManagementRepository
import com.p4r4d0x.skintker.data.repository.LogsManagementRepositoryImpl
import com.p4r4d0x.skintker.data.repository.LogsRepository
import com.p4r4d0x.skintker.data.room.DailyLogDao
import com.p4r4d0x.skintker.data.room.LogsDatabase
import com.p4r4d0x.skintker.domain.usecases.*
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
    viewModel { SurveyViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }

}
val repositoriesModule = module {
    factory { LogsRepository() }
    factory { FirebaseLogsManagementDataSource() }
    factory<LogsManagementRepository> { LogsManagementRepositoryImpl(get(), get()) }
}

val useCasesModule = module {
    factory { AddLogUseCase(get()) }
    factory { GetLogUseCase(get()) }
    factory { GetLogsUseCase(get()) }
    factory { GetQueriedLogsUseCase(get()) }
    factory { ExportLogsDBUseCase(get()) }
    factory { ImportLogsDBUseCase(get()) }
    factory { UpdateDdbbUseCase(get(), get()) }
}

val databasesModule = module {
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

}

val networkingModule = module {
//        single { GsonConverterFactory.create() as Converter.Factory }
//        single { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) as Interceptor }
//        single {
//            OkHttpClient.Builder().apply {
//                if (BuildConfig.DEBUG) addInterceptor(get())
//                    .callTimeout(10, TimeUnit.SECONDS)
//            }.build()
//        }
//        single {
//            Retrofit.Builder()
//                .baseUrl(BuildConfig.HOST)
//                .client(get())
//                .addConverterFactory(get())
//                .build()
//        }

//        single { get<Retrofit>().create(LoginService::class.java) }
//        single { get<Retrofit>().create(BlockService::class.java) }
//        single { get<Retrofit>().create(ForgotPasswordService::class.java) }
}

