package com.yourname.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yourname.weather.data.UserPreferences
import com.yourname.weather.data.WeatherInfo
import com.yourname.weather.ui.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen()
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val userPreferences by viewModel.userPreferences.collectAsState()
    val currentWeather by viewModel.currentWeather.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    var cityInput by remember { mutableStateOf("") }
    var unitInput by remember { mutableStateOf("C") }

    // Simulated user ID for testing (in a real app this would come from Auth)
    val testUserId = "user_123"

    // Load preferences when screen launches
    LaunchedEffect(Unit) {
        viewModel.loadPreferences(testUserId)
    }

    // Update local fields when Supabase data arrives
    LaunchedEffect(userPreferences) {
        userPreferences?.let {
            cityInput = it.favoriteCity
            unitInput = it.temperatureUnit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "My Weather App",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Weather display card if live weather is available
        currentWeather?.let { weather ->
            WeatherCard(weather = weather)
            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedTextField(
            value = cityInput,
            onValueChange = { cityInput = it },
            label = { Text("Favorite City") },
            placeholder = { Text("e.g. New York, London, Tokyo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = unitInput,
            onValueChange = { unitInput = it },
            label = { Text("Temperature Unit (C or F)") },
            placeholder = { Text("C or F") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val newPrefs = UserPreferences(
                        id = testUserId,
                        favoriteCity = cityInput,
                        temperatureUnit = unitInput
                    )
                    viewModel.savePreferences(newPrefs)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save & Fetch")
            }

            OutlinedButton(
                onClick = {
                    viewModel.fetchWeather(cityInput, unitInput)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Check Weather")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
        }

        statusMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun WeatherCard(weather: WeatherInfo) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val locationText = if (!weather.country.isNullOrBlank()) {
                "${weather.cityName}, ${weather.country}"
            } else {
                weather.cityName
            }

            Text(
                text = locationText,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${weather.temperature} ${weather.temperatureUnit}",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = weather.condition,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "💧 Humidity: ${weather.humidity}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "💨 Wind: ${weather.windSpeed} km/h",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    MaterialTheme {
        WeatherScreen()
    }
}
