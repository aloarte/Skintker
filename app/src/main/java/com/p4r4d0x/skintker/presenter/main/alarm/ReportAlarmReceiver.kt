package com.p4r4d0x.skintker.presenter.main.alarm

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants.CHANNEL_ID
import com.p4r4d0x.skintker.data.Constants.NOTIFICATION_ID
import com.p4r4d0x.skintker.presenter.common.utils.AlarmUtils.vibratePattern
import com.p4r4d0x.skintker.presenter.main.MainActivity
import com.p4r4d0x.skintker.theme.SoftRedDark

class ReportAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, getNotificationBuilder(context).build())
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun getNotificationBuilder(context: Context): NotificationCompat.Builder {
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_notification)
            .setColor(SoftRedDark.toArgb())
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_description))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(context.getString(R.string.notification_description_full))
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(
                PendingIntent.getActivity(
                    context, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setVibrate(vibratePattern)
    }
}