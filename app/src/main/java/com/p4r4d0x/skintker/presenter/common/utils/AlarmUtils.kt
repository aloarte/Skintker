package com.p4r4d0x.skintker.presenter.common.utils

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

    fun getTimeForAlarm(): Long {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 20
        calendar[Calendar.MINUTE] = 43
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        val cur = Calendar.getInstance()
        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1)
        }
        return calendar.timeInMillis
    }
}