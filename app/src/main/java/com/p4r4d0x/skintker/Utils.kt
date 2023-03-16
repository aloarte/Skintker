package com.p4r4d0x.skintker

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.p4r4d0x.domain.utils.Constants

fun getHumidityString(value: Int) =
    when (value) {
        0, 1 -> R.string.humidity_1
        2, 3 -> R.string.humidity_2
        4, 5, 6 -> R.string.humidity_3
        7, 8 -> R.string.humidity_4
        9, 10 -> R.string.humidity_5
        else -> R.string.humidity_3
    }

fun getTemperatureString(value: Int) =
    when (value) {
        0, 1 -> R.string.temperature_1
        2, 3 -> R.string.temperature_2
        4, 5, 6 -> R.string.temperature_3
        7, 8 -> R.string.temperature_4
        9, 10 -> R.string.temperature_5
        else -> R.string.temperature_3
    }

fun getAlcoholLevel(alcoholLevel: Int): Int =
    when (alcoholLevel) {
        0 -> R.string.card_no_alcohol
        1 -> R.string.card_any_alcohol_beer
        2 -> R.string.card_any_alcohol_wine
        3 -> R.string.card_quite_alcohol
        else -> R.string.card_no_alcohol
    }

fun getUserId(activity: Activity?): String {
    val prefs: SharedPreferences? =
        activity?.getSharedPreferences(Constants.SKITNKER_PREFERENCES, Context.MODE_PRIVATE)
    return prefs?.getString(Constants.PREFERENCES_USER_ID, "") ?: ""
}