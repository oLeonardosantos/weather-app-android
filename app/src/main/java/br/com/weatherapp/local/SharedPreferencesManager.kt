package br.com.weatherapp.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedPreferencesManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val PREFS_NAME = "WeatherAppPrefs"
        private const val CITY_NAME_KEY = "city_name"
    }

    /**
     * Save the city name asynchronously on the IO thread
     */
    suspend fun saveCityName(cityName: String) {
        withContext(Dispatchers.IO) {
            val editor = sharedPreferences.edit()
            editor.putString(CITY_NAME_KEY, cityName)
            editor.apply() // Apply asynchronously
        }
    }

    /**
     * Get the saved city name on the IO thread, default is "Unknown City"
     */
    suspend fun getCityName(): String {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getString(CITY_NAME_KEY, "Unknown City") ?: "Unknown City"
        }
    }
}