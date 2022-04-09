package com.p4r4d0x.skintker.presenter.common.utils

import android.content.Context
import android.util.Log
import com.p4r4d0x.skintker.data.Constants
import java.util.*

object AlarmUtils {

    const val ALARM1_ID = 10000
    val vibratePattern = longArrayOf(
        1000,
        1000,
        1000,
        1000,
        1000
    )

    fun getTimeForAlarm(context: Context?): Long {
        val preferences =
            context?.getSharedPreferences(Constants.SKITNKER_PREFERENCES, Context.MODE_PRIVATE)
        val calendar = Calendar.getInstance()

        val a = preferences?.getInt(Constants.PREFERENCES_ALARM_HOUR, 0) ?: 0
        val b = preferences?.getInt(Constants.PREFERENCES_ALARM_MINUTES, 0) ?: 0
        Log.d("ALRALR", "getTimeForAlarm  $a , $b")
        calendar[Calendar.HOUR_OF_DAY] =
            preferences?.getInt(Constants.PREFERENCES_ALARM_HOUR, 0) ?: 0
        calendar[Calendar.MINUTE] = preferences?.getInt(Constants.PREFERENCES_ALARM_MINUTES, 0) ?: 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        val cur = Calendar.getInstance()
        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1)
        }
        return calendar.timeInMillis
    }
}