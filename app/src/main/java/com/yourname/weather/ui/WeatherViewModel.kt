package com.yourname.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourname.weather.data.UserPreferences
import com.yourname.weather.data.WeatherInfo
import com.yourname.weather.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    // Reactive state for user preferences
    private val _userPreferences = MutableStateFlow<UserPreferences?>(null)
    val userPreferences: StateFlow<UserPreferences?> = _userPreferences.asStateFlow()

    // Reactive state for current weather fetched from API
    private val _currentWeather = MutableStateFlow<WeatherInfo?>(null)
    val currentWeather: StateFlow<WeatherInfo?> = _currentWeather.asStateFlow()

    // Loading state indicator
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Status / feedback message
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    // Load preferences from Supabase and fetch corresponding live weather
    fun loadPreferences(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val prefs = repository.getUserPreferences(userId)
                _userPreferences.value = prefs
                if (prefs != null && prefs.favoriteCity.isNotBlank()) {
                    val weather = repository.fetchWeather(prefs.favoriteCity, prefs.temperatureUnit)
                    _currentWeather.value = weather
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Save or update preferences in Supabase and refresh weather
    fun savePreferences(preferences: UserPreferences) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.savePreferences(preferences)
                _userPreferences.value = preferences
                val weather = repository.fetchWeather(preferences.favoriteCity, preferences.temperatureUnit)
                _currentWeather.value = weather
                _statusMessage.value = if (weather != null) "Weather updated successfully!" else "Saved to Supabase, but could not find city weather."
            } catch (e: Exception) {
                e.printStackTrace()
                _statusMessage.value = "Failed to save preferences."
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Manually fetch weather for a specific city and unit
    fun fetchWeather(city: String, unit: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _statusMessage.value = null
            try {
                val weather = repository.fetchWeather(city, unit)
                _currentWeather.value = weather
                if (weather == null) {
                    _statusMessage.value = "City not found. Please verify the name."
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _statusMessage.value = "Error fetching weather data."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
