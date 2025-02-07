package com.example.appmeteo.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.appmeteo.data.model.Ville
import com.example.appmeteo.ui.components.WeatherInfo
import com.example.appmeteo.viewmodel.DetailsViewModel
import com.example.appmeteo.viewmodel.FavoritesViewModel
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState


@Composable
fun HomeScreen(
    favoritesViewModel: FavoritesViewModel,
    detailsViewModel: DetailsViewModel,
    modifier: Modifier = Modifier
) {
    val favorites = favoritesViewModel.favorites.collectAsState()
    val weatherByCity = detailsViewModel.weatherByCity.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        CurrentLocationWeather(
            context = context,
            detailsViewModel = detailsViewModel,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Favorite Cities", modifier = Modifier.padding(8.dp))

        if (favorites.value.isEmpty()) {
            Text(text = "No favorites yet", modifier = Modifier.padding(8.dp))
        } else {
            favorites.value.forEach { city ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(1.dp, Color.Gray)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "${city.name}, ${city.country}")

                        LaunchedEffect(city) {
                            detailsViewModel.fetchWeather(city.name, city.latitude, city.longitude)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        weatherByCity.value[city.name]?.let {
                            WeatherInfo(it)
                        } ?: Text(text = "Loading cached weather...")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "★ Remove from Favorites",
                            color = Color.Red,
                            modifier = Modifier.clickable {
                                favoritesViewModel.removeFavorite(city)
                            }
                        )
                    }
                }
            }
        }
    }
}
