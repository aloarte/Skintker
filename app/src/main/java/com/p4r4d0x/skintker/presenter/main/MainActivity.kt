package com.p4r4d0x.skintker.presenter.main

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants.CHANNEL_DESCRIPTION
import com.p4r4d0x.skintker.data.Constants.CHANNEL_ID
import com.p4r4d0x.skintker.data.Constants.CHANNEL_NAME
import com.p4r4d0x.skintker.presenter.common.utils.AlarmUtils.ALARM1_ID
import com.p4r4d0x.skintker.presenter.common.utils.AlarmUtils.getTimeForAlarm
import com.p4r4d0x.skintker.presenter.main.alarm.ReportAlarmReceiver
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Location mgr
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        //Navigation
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        createNotificationChannel()
    }

    fun getLocation(cityObtained: (String) -> Unit) {
        try {
            // Request location updates
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                object :
                    LocationListener {
                    override fun onLocationChanged(location: Location) {
                        val gcd = Geocoder(this@MainActivity, Locale.getDefault())
                        val addresses: List<Address> =
                            gcd.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses.isNotEmpty()) {
                            cityObtained.invoke(addresses[0].locality)
                        }
                    }

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                    }

                    override fun onProviderEnabled(provider: String) {
                    }

                    override fun onProviderDisabled(provider: String) {
                    }
                })
        } catch (ex: SecurityException) {
        }
    }

    private fun createNotificationChannel() {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            })
        setAlarm()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarm() {
        val alarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            getTimeForAlarm(),
            AlarmManager.INTERVAL_DAY,
            PendingIntent.getBroadcast(
                this,
                ALARM1_ID,
                Intent(this, ReportAlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }
}