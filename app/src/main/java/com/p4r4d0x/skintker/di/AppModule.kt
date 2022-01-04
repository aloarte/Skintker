package com.p4r4d0x.skintker.di

import android.app.Application
import androidx.room.Room
import com.p4r4d0x.skintker.data.repository.LogManagementRepository
import com.p4r4d0x.skintker.data.repository.LogManagementRepositoryImpl
import com.p4r4d0x.skintker.data.repository.LogsRepository
import com.p4r4d0x.skintker.data.room.DailyLogDao
import com.p4r4d0x.skintker.data.room.LogsDatabase
import com.p4r4d0x.skintker.domain.usecases.AddLogUseCase
import com.p4r4d0x.skintker.domain.usecases.GetLogUseCase
import com.p4r4d0x.skintker.domain.usecases.GetLogsUseCase
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import com.p4r4d0x.skintker.presenter.welcome.viewmodel.WelcomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel { WelcomeViewModel(get()) }
    viewModel { SurveyViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}
val repositoriesModule = module {
    factory { LogsRepository() }
    factory<LogManagementRepository> { LogManagementRepositoryImpl(get()) }
}

val useCasesModule = module {
    factory { AddLogUseCase(get()) }
    factory { GetLogUseCase(get()) }
    factory { GetLogsUseCase(get()) }
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

