package br.com.weatherapp.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import br.com.weatherapp.R
import br.com.weatherapp.local.SharedPreferencesManager
import br.com.weatherapp.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var loading: ProgressBar
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loading = findViewById(R.id.loading)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPreferencesManager = SharedPreferencesManager(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocationCity()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationCity() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val cityName = getCityNameFromLocation(location)
                    saveCityName(cityName)

                    launch(Dispatchers.Main) {
                        loading.visibility = View.GONE
                        openMainActivity()
                    }
                }
            } else {
                requestNewLocationData()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val  locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, INTERVAL)
            .setMaxUpdates(ONE_RESULT)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    location?.let {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val cityName = getCityNameFromLocation(it)
                            saveCityName(cityName)

                            launch(Dispatchers.Main) {
                                loading.visibility = View.GONE
                                openMainActivity()
                            }
                        }
                    }
                }
            },
            null
        )
    }


    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        this@SplashActivity.startActivity(intent)
        finish()
    }

    private fun getCityNameFromLocation(location: Location): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        var cityName = ""
        if(addresses?.get(POSITION_ZERO)?.locality != null) {
            cityName = addresses?.get(POSITION_ZERO)?.locality ?: "Unknown City"
        } else {
            cityName = addresses?.get(POSITION_ZERO)?.subAdminArea ?: "Unknown City"
        }

        return cityName
    }


    private fun saveCityName(cityName: String) {
        lifecycleScope.launch {
            sharedPreferencesManager.saveCityName(cityName)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[POSITION_ZERO] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationCity()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val POSITION_ZERO = 0
        private const val ONE_RESULT = 1
        private const val INTERVAL = 1000L
    }
}