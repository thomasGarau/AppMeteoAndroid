package com.example.appmeteo.ui.search

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmeteo.data.model.Ville
import com.example.appmeteo.ui.components.CityCard
import com.example.appmeteo.ui.components.WeatherInfo
import com.example.appmeteo.viewmodel.DetailsViewModel
import com.example.appmeteo.viewmodel.FavoritesViewModel
import com.example.appmeteo.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel(),
    detailsViewModel: DetailsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<Ville?>(null) }
    val cities by viewModel.cities.collectAsState()
    val favorites by favoritesViewModel.favorites.collectAsState() // Obtenir les favoris en temps réel
    val weather by detailsViewModel.weatherByCity.collectAsState()

    // Réinitialiser la recherche et les résultats avec LaunchedEffect
    LaunchedEffect(Unit) {
        query = ""
        selectedCity = null
        viewModel.clearCities() // Assurez-vous d'avoir ajouté cette méthode dans `SearchViewModel`
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barre de recherche
        SearchBar(
            query = query,
            onQueryChange = {
                query = it
                selectedCity = null // Réinitialise la sélection lors de la modification de la recherche
                if (query.length > 2) viewModel.searchCity(query)
            },
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Liste des résultats de la recherche
        if (selectedCity == null && query.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(1.dp, Color.Gray)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Utilisation de `cities.map` pour afficher chaque ville
                    cities.map { city ->
                        CityCard(
                            city = city,
                            isFavorite = favorites.contains(city), // Vérifier dans les favoris en temps réel
                            onCityClick = {
                                selectedCity = city
                                detailsViewModel.fetchWeather(city.name, city.latitude, city.longitude)
                            },
                            onFavoriteClick = {
                                if (favorites.contains(city)) {
                                    favoritesViewModel.removeFavorite(city)
                                } else {
                                    favoritesViewModel.addFavorite(city)
                                }
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Affichage des détails de la ville sélectionnée
        selectedCity?.let { city ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(1.dp, Color.Gray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Name: ${city.name}")
                    Text(text = "Country: ${city.country}")
                    weather[city.name]?.let { WeatherInfo(it) } ?: Text(text = "Loading weather...")

                    Spacer(modifier = Modifier.height(8.dp))

                    // Étoile pour ajouter ou retirer des favoris
                    Text(
                        text = if (favorites.contains(city)) "★ Remove from Favorites" else "☆ Add to Favorites",
                        color = if (favorites.contains(city)) Color.Yellow else Color.Gray,
                        modifier = Modifier.clickable {
                            if (favorites.contains(city)) {
                                favoritesViewModel.removeFavorite(city)
                            } else {
                                favoritesViewModel.addFavorite(city)
                            }
                        }
                    )
                }
            }
        }
    }
}
