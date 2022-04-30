package com.p4r4d0x.test

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module


class KoinTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        stopKoin()
        startKoin {
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