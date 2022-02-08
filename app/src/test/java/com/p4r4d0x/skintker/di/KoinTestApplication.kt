package com.p4r4d0x.skintker.di

import android.app.Application
import com.p4r4d0x.skintker.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.logger.Level
import org.koin.core.module.Module

class KoinTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@KoinTestApplication)
            modules(emptyList())
        }
    }

    fun injectModule(modules: Array<out Module>) {
        modules.forEach {
            loadKoinModules(it)
        }
    }

    fun unloadModules(modules: Array<out Module>) {
        modules.forEach {
            unloadKoinModules(it)
        }
    }
}