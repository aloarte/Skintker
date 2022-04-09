package com.p4r4d0x.skintker.presenter.main.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.p4r4d0x.skintker.data.Constants.BOOT_INTENT
import com.p4r4d0x.skintker.presenter.common.utils.AlarmUtils.ALARM1_ID
import com.p4r4d0x.skintker.presenter.common.utils.AlarmUtils.getTimeForAlarm

class WakeUpAlarmReceiver : BroadcastReceiver() {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BOOT_INTENT) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                getTimeForAlarm(),
                AlarmManager.INTERVAL_DAY,
                PendingIntent.getBroadcast(
                    context,
                    ALARM1_ID,
                    Intent(context, ReportAlarmReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        }
    }
}