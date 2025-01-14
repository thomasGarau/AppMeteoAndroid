package com.example.appmeteo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmeteo.data.model.MeteoResponse

@Composable
fun WeatherInfo(
    weather: MeteoResponse,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Temperature: ${weather.hourly.temperature_2m.first()} °C")
        Text(text = "Humidity: ${weather.hourly.relative_humidity_2m.first()} %")
        Text(text = "Wind Speed: ${weather.hourly.wind_speed_10m.first()} km/h")
    }
}
