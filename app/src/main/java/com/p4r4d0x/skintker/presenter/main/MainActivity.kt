package com.p4r4d0x.skintker.presenter.main

import android.location.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.p4r4d0x.skintker.R
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

}