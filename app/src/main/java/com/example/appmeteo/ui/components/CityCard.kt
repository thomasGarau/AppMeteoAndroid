package com.example.appmeteo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appmeteo.data.model.Ville

@Composable
fun CityCard(
    city: Ville,
    isFavorite: Boolean, // Utilisé dans l'icône d'étoile
    onCityClick: () -> Unit,
    onFavoriteClick: () -> Unit, // Action pour ajouter/retirer des favoris
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onCityClick() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Affichage du nom de la ville et du pays
        Column(modifier = Modifier.weight(1f)) {
            Text(text = city.name, modifier = Modifier.padding(start = 8.dp))
            Text(
                text = city.country,
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Gray
            )
        }
        // Affichage de l'icône favori
        Text(
            text = if (isFavorite) "★" else "☆",
            color = if (isFavorite) Color.Yellow else Color.Gray,
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable { onFavoriteClick() }
        )
    }
}
