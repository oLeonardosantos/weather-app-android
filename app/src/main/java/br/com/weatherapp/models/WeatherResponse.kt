package br.com.weatherapp.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val current: Current
)

data class Location(
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    @SerializedName("tz_id") val timezoneId: String,
    @SerializedName("localtime_epoch") val localtimeEpoch: Long,
    @SerializedName("localtime") val localtime: String
)

data class Current(
    @SerializedName("last_updated_epoch") val lastUpdatedEpoch: Long,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("temp_c") val tempCelsius: Double,
    @SerializedName("temp_f") val tempFahrenheit: Double,
    @SerializedName("is_day") val isDay: Int,
    @SerializedName("condition") val condition: Condition,
    @SerializedName("wind_mph") val windMph: Double,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_degree") val windDegree: Int,
    @SerializedName("wind_dir") val windDirection: String,
    @SerializedName("pressure_mb") val pressureMb: Double,
    @SerializedName("pressure_in") val pressureInches: Double,
    @SerializedName("precip_mm") val precipMm: Double,
    @SerializedName("precip_in") val precipInches: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("cloud") val cloud: Int,
    @SerializedName("feelslike_c") val feelsLikeCelsius: Double,
    @SerializedName("feelslike_f") val feelsLikeFahrenheit: Double,
    @SerializedName("windchill_c") val windChillCelsius: Double,
    @SerializedName("windchill_f") val windChillFahrenheit: Double,
    @SerializedName("heatindex_c") val heatIndexCelsius: Double,
    @SerializedName("heatindex_f") val heatIndexFahrenheit: Double,
    @SerializedName("dewpoint_c") val dewPointCelsius: Double,
    @SerializedName("dewpoint_f") val dewPointFahrenheit: Double,
    @SerializedName("vis_km") val visibilityKm: Double,
    @SerializedName("vis_miles") val visibilityMiles: Double,
    @SerializedName("uv") val uv: Double,
    @SerializedName("gust_mph") val gustMph: Double,
    @SerializedName("gust_kph") val gustKph: Double
)

data class Condition(
    @SerializedName("text") val text: String,
    @SerializedName("icon") val iconUrl: String,
    @SerializedName("code") val code: Int
) {
    fun getImage() = "https://$iconUrl"
}