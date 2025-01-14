package com.example.appmeteo.ui.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appmeteo.ui.components.WeatherInfo
import com.example.appmeteo.viewmodel.DetailsViewModel
import com.google.android.gms.location.LocationServices
@Composable
fun CurrentLocationWeather(
    context: Context,
    detailsViewModel: DetailsViewModel,
    modifier: Modifier = Modifier
) {
    val weatherByCity = detailsViewModel.weatherByCity.collectAsState()
    var locationError by remember { mutableStateOf<String?>(null) }

    // Déclencher la récupération des données météo pour l'emplacement actuel
    LaunchedEffect(Unit) {
        fetchCurrentLocation(context,
            onLocationReceived = { latitude, longitude ->
                detailsViewModel.fetchWeather("current_location", latitude, longitude)
            },
            onError = {
                locationError = it
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (locationError != null) {
                Text(text = locationError ?: "Error", color = Color.Red)
            } else {
                // Récupérer les données météo pour la clé "current_location"
                weatherByCity.value["current_location"]?.let {
                    Text(text = "Current Location Weather")
                    WeatherInfo(it)
                } ?: Text(text = "Loading cached weather...")
            }
        }
    }
}



@SuppressLint("MissingPermission")
fun fetchCurrentLocation(
    context: Context,
    onLocationReceived: (Double, Double) -> Unit,
    onError: (String) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                onLocationReceived(location.latitude, location.longitude)
            } else {
                onError("Unable to retrieve location. Please enable location services.")
            }
        }
        .addOnFailureListener { exception ->
            onError("Failed to get location: ${exception.message}")
        }
}
