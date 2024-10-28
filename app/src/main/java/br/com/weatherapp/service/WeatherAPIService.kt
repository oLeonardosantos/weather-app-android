package br.com.weatherapp.service

import br.com.weatherapp.models.WeatherResponse
import br.com.weatherapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

interface WeatherAPIService {
    /**
     * API para buscar os dados do clima
     */
    @GET("v1/current.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String = BuildConfig.WEATHER_API,
        @Query("q") location: String,
        @Query("aqi") aqi: String = "no",
        @Query("lang") lang: String = Locale.getDefault().language
    ): WeatherResponse
}