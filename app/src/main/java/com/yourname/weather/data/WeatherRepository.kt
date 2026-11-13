package com.yourname.weather.data

import io.github.jan.supabase.postgrest.from
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class WeatherRepository {

    private val supabase = SupabaseClient.client

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    // Get a user's preferences by their ID from Supabase
    suspend fun getUserPreferences(userId: String): UserPreferences? {
        return try {
            supabase.from("user_preferences")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingleOrNull<UserPreferences>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Save or update user preferences in Supabase
    suspend fun savePreferences(preferences: UserPreferences) {
        try {
            supabase.from("user_preferences")
                .upsert(preferences)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Fetch live weather data using Open-Meteo API
    suspend fun fetchWeather(cityName: String, unit: String): WeatherInfo? {
        if (cityName.isBlank()) return null
        return try {
            // Step 1: Geocode city name to coordinates
            val geocodingUrl = "https://geocoding-api.open-meteo.com/v1/search"
            val geoResponse = httpClient.get(geocodingUrl) {
                parameter("name", cityName.trim())
                parameter("count", 1)
                parameter("language", "en")
                parameter("format", "json")
            }.body<GeocodingResponse>()

            val location = geoResponse.results?.firstOrNull() ?: return null

            // Step 2: Fetch current weather for latitude & longitude
            val isFahrenheit = unit.trim().equals("F", ignoreCase = true)
            val tempUnitParam = if (isFahrenheit) "fahrenheit" else "celsius"
            val displayUnit = if (isFahrenheit) "°F" else "°C"

            val weatherUrl = "https://api.open-meteo.com/v1/forecast"
            val weatherResponse = httpClient.get(weatherUrl) {
                parameter("latitude", location.latitude)
                parameter("longitude", location.longitude)
                parameter("current", "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m")
                parameter("temperature_unit", tempUnitParam)
            }.body<WeatherApiResponse>()

            val current = weatherResponse.current ?: return null

            WeatherInfo(
                cityName = location.name,
                country = location.country,
                temperature = current.temperature,
                temperatureUnit = displayUnit,
                condition = parseWeatherCode(current.weatherCode),
                humidity = current.humidity,
                windSpeed = current.windSpeed
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
