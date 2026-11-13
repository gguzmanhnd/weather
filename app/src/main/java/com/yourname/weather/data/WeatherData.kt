package com.yourname.weather.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeocodingResponse(
    @SerialName("results")
    val results: List<GeocodingResult>? = null
)

@Serializable
data class GeocodingResult(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null
)

@Serializable
data class WeatherApiResponse(
    val current: CurrentWeather? = null
)

@Serializable
data class CurrentWeather(
    @SerialName("temperature_2m")
    val temperature: Double,
    @SerialName("relative_humidity_2m")
    val humidity: Int,
    @SerialName("weather_code")
    val weatherCode: Int,
    @SerialName("wind_speed_10m")
    val windSpeed: Double
)

data class WeatherInfo(
    val cityName: String,
    val country: String?,
    val temperature: Double,
    val temperatureUnit: String,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double
)

fun parseWeatherCode(code: Int): String {
    return when (code) {
        0 -> "Clear sky ☀️"
        1 -> "Mainly clear 🌤️"
        2 -> "Partly cloudy ⛅"
        3 -> "Overcast ☁️"
        45, 48 -> "Foggy 🌫️"
        51, 53, 55 -> "Drizzle 🌦️"
        61, 63, 65 -> "Rain 🌧️"
        71, 73, 75 -> "Snow ❄️"
        80, 81, 82 -> "Rain showers 🌧️"
        95, 96, 99 -> "Thunderstorm ⛈️"
        else -> "Cloudy 🌥️"
    }
}
