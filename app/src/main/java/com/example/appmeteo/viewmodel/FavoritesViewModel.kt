package com.example.appmeteo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmeteo.data.model.Ville
import com.example.appmeteo.utils.CacheManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(context: Context) : ViewModel() {
    private val cacheManager = CacheManager(context)

    private val _favorites = MutableStateFlow<List<Ville>>(emptyList())
    val favorites: StateFlow<List<Ville>> = _favorites

    init {
        // Charger les favoris du cache au démarrage
        viewModelScope.launch {
            _favorites.value = cacheManager.loadFavorites()
        }
    }

    fun addFavorite(ville: Ville) {
        if (!_favorites.value.contains(ville)) {
            _favorites.value = _favorites.value.toMutableList().apply { add(ville) }
            saveToCache() // Sauvegarder les favoris dans le cache
        }
    }

    fun removeFavorite(ville: Ville) {
        _favorites.value = _favorites.value.toMutableList().apply { remove(ville) }
        saveToCache() // Sauvegarder les favoris dans le cache
    }

    fun isFavorite(ville: Ville): Boolean {
        return _favorites.value.contains(ville)
    }

    private fun saveToCache() {
        cacheManager.saveFavorites(_favorites.value)
    }
}
