package com.p4r4d0x.skintker.presenter.main

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.navigation.NavigationView
import com.p4r4d0x.domain.utils.Constants.CHANNEL_DESCRIPTION
import com.p4r4d0x.domain.utils.Constants.CHANNEL_ID
import com.p4r4d0x.domain.utils.Constants.CHANNEL_NAME
import com.p4r4d0x.skintker.R
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Navigation
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        //Location mgr
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //Notifications
        createNotificationChannel()
    }


    private fun getCityFromLocation(location: Location, cityObtained: (String) -> Unit) {
        val gcd = Geocoder(this@MainActivity, Locale.getDefault())
        val addresses: List<Address>? =
            gcd.getFromLocation(location.latitude, location.longitude, 1)?.toList()
        addresses?.getOrNull(0)?.let {
            cityObtained.invoke(it.locality)
        }
    }

    fun isGpsEnabled() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    /**
     * The strategy for the location is the following:
     *  1 - Check permission
     *  2 - Check if the GPS is enabled
     *  3 - Get location from lastLocation (fast obtained from any previous gps source)
     *  4 - If lastLocation failed get gps from getCurrentLocation (triggers the provider
     *  to obtain the location and have a little delay)
     */
    fun getLocation(cityObtained: (String) -> Unit) {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        getCityFromLocation(location, cityObtained)
                    } ?: run {
                        fusedLocationClient.getCurrentLocation(
                            Priority.PRIORITY_HIGH_ACCURACY,
                            object : CancellationToken() {
                                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                                    CancellationTokenSource().token

                                override fun isCancellationRequested() = false
                            })
                            .addOnSuccessListener { location: Location? ->
                                location?.let {
                                    getCityFromLocation(location, cityObtained)
                                }

                            }
                    }
                }

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
    }
}