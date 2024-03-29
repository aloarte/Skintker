package com.p4r4d0x.skintker.presenter.main.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.p4r4d0x.domain.utils.Constants.BOOT_INTENT
import com.p4r4d0x.skintker.presenter.common.utils.AlarmUtils

class WakeUpAlarmReceiver : BroadcastReceiver() {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BOOT_INTENT) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                AlarmUtils.getTimeForAlarm(context),
                AlarmManager.INTERVAL_DAY,
                PendingIntent.getBroadcast(
                    context,
                    AlarmUtils.ALARM1_ID,
                    Intent(context, ReportAlarmReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }
}