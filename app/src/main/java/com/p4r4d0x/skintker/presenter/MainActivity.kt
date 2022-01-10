package com.p4r4d0x.skintker.presenter

import android.location.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.p4r4d0x.skintker.R
import java.util.*


class MainActivity : AppCompatActivity() {

    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Create persistent LocationManager reference
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
//        getLocation(){}

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    fun getLocation(cityObtained: (String) -> Unit) {
        Log.d("ALRALR", "getLocation")

        try {
            // Request location updates
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                object :
                    LocationListener {
                    override fun onLocationChanged(location: Location) {
                        Log.d("ALRALR", "onLocationChanged: $location")
                        val gcd = Geocoder(this@MainActivity, Locale.getDefault())
                        val addresses: List<Address> =
                            gcd.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses.isNotEmpty()) {
                            Log.d("ALRALR", "locality: ${addresses[0].locality}")
                            cityObtained.invoke(addresses[0].locality)
                        }
                    }

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                        Log.d("ALRALR", "onStatusChanged")

                    }

                    override fun onProviderEnabled(provider: String) {
                        Log.d("ALRALR", "onProviderEnabled")

                    }

                    override fun onProviderDisabled(provider: String) {
                        Log.d("ALRALR", "onProviderDisabled")

                    }
                })
        } catch (ex: SecurityException) {
            Log.d("ALRALR", "Security Exception, no location available")
        }
    }

}