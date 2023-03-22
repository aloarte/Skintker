package com.p4r4d0x.skintker.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SkintkerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SkintkerApplication)
            modules(
                vmModule,
                useCasesModule,
                repositoriesModule,
                datasourcesModule,
                networkModule,
                externalModule,
                databaseModule
            )
        }
    }
}