package com.p4r4d0x.test

import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.koin.core.module.Module
import org.koin.test.AutoCloseKoinTest

open class KoinBaseTest(private vararg val modules: Module = emptyArray()) : AutoCloseKoinTest() {
    private lateinit var app: KoinTestApplication

    @Before
    fun init() {
        app = ApplicationProvider.getApplicationContext() as KoinTestApplication
        app.injectModule(modules)
    }

    @After
    fun finish() {
        app.unloadModules(modules)
    }
}