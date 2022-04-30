package com.p4r4d0x.test

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class KoinTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, KoinTestApplication::class.java.name, context)
    }
}