package com.p4r4d0x.skintker.presenter.view

import androidx.annotation.StringRes
import com.p4r4d0x.skintker.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Log : Screen("profile", R.string.screen_log)
    object Resume : Screen("resume", R.string.screen_resume)
    object History : Screen("history", R.string.screen_history)

}
