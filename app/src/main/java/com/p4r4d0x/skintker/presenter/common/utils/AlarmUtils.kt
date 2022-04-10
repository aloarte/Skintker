package com.p4r4d0x.skintker.presenter.common.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.presenter.main.alarm.ReportAlarmReceiver
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
        with(context?.getSharedPreferences(Constants.SKITNKER_PREFERENCES, Context.MODE_PRIVATE)) {
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = this?.getInt(Constants.PREFERENCES_ALARM_HOUR, 0) ?: 0
            calendar[Calendar.MINUTE] = this?.getInt(Constants.PREFERENCES_ALARM_MINUTES, 0) ?: 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            val cur = Calendar.getInstance()
            if (cur.after(calendar)) {
                calendar.add(Calendar.DATE, 1)
            }
            return calendar.timeInMillis
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    fun setAlarm(activity: FragmentActivity) {
        val alarmManager =
            activity.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            getTimeForAlarm(activity),
            AlarmManager.INTERVAL_DAY,
            PendingIntent.getBroadcast(
                activity,
                ALARM1_ID,
                Intent(activity, ReportAlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun cancelAlarm(activity: FragmentActivity, preferences: SharedPreferences) {
        val alarmManager =
            activity.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                activity, ALARM1_ID, Intent(
                    activity,
                    ReportAlarmReceiver::class.java
                ),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
        clearAlarmPreferences(preferences)
    }

    private fun clearAlarmPreferences(preferences: SharedPreferences) {
        preferences.edit()
            .remove(Constants.PREFERENCES_ALARM_HOUR)
            .remove(Constants.PREFERENCES_ALARM_MINUTES)
            .remove(Constants.PREFERENCES_ALARM_CREATED)
            .apply()
    }

    fun addAlarmPreferences(hour: Int, minute: Int, preferences: SharedPreferences) {
        preferences.edit()?.let {
            it.putInt(Constants.PREFERENCES_ALARM_HOUR, hour)
                ?.putInt(Constants.PREFERENCES_ALARM_MINUTES, minute)
                ?.putBoolean(Constants.PREFERENCES_ALARM_CREATED, true)?.apply()
        }
    }

}