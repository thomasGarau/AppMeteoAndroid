package com.example.appmeteo.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmeteo.AppModule
import com.example.appmeteo.data.api.MeteoApi
import com.example.appmeteo.data.model.MeteoResponse
import com.example.appmeteo.utils.CacheManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(private val context: Context) : ViewModel() {
    private val cacheManager = CacheManager(context)

    // Map pour stocker les données météo par ville
    private val _weatherByCity = MutableStateFlow<Map<String, MeteoResponse>>(emptyMap())
    val weatherByCity: StateFlow<Map<String, MeteoResponse>> = _weatherByCity

    fun fetchWeather(cityName: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                // Charger les données du cache
                val cachedData = cacheManager.getWeatherData(cityName)
                if (cachedData != null) {
                    _weatherByCity.value = _weatherByCity.value.toMutableMap().apply {
                        put(cityName, cachedData)
                    }
                }

                // Faire un appel API pour mettre à jour les données
                val response = AppModule.provideMeteoApi().getWeather(latitude, longitude)
                _weatherByCity.value = _weatherByCity.value.toMutableMap().apply {
                    put(cityName, response)
                }

                cacheManager.saveWeatherData(cityName, response)
            } catch (e: Exception) {
                // Si l'API échoue, conserver les données en cache
                val cachedData = cacheManager.getWeatherData(cityName)
                if (cachedData != null) {
                    _weatherByCity.value = _weatherByCity.value.toMutableMap().apply {
                        put(cityName, cachedData)
                    }
                } else {
                    // Supprimer la ville si aucune donnée disponible
                    _weatherByCity.value = _weatherByCity.value.toMutableMap().apply {
                        remove(cityName)
                    }
                }
            }
        }
    }
}

