package com.yourname.weather.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    @SerialName("id")
    val id: String,

    @SerialName("favorite_city")
    val favoriteCity: String,

    @SerialName("temperature_unit")
    val temperatureUnit: String // E.g.: "C" or "F"
)
