package com.p4r4d0x.skintker.di

import android.app.Application
import androidx.room.Room
import com.p4r4d0x.data.room.DailyLogDao
import com.p4r4d0x.data.room.LogsDatabase
import com.p4r4d0x.skintker.BuildConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val databaseModule = module {
    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }
}

fun provideDataBase(application: Application): LogsDatabase {
    return Room.databaseBuilder(application, LogsDatabase::class.java, BuildConfig.DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()
}

fun provideDao(dataBase: LogsDatabase): DailyLogDao {
    return dataBase.dailyLogDao()
}
