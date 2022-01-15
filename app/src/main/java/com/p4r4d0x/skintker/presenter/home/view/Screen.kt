package com.p4r4d0x.skintker.presenter.home.view

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarViewMonth
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.ui.graphics.vector.ImageVector
import com.p4r4d0x.skintker.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Resume : Screen("resume", R.string.screen_resume, Icons.Rounded.Insights)
    object History : Screen("history", R.string.screen_history, Icons.Rounded.CalendarViewMonth)

}
