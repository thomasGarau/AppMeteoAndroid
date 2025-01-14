package com.example.appmeteo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmeteo.data.api.GeocodingApi
import com.example.appmeteo.data.model.Ville
import com.example.appmeteo.data.repository.MeteoRepository
import com.example.appmeteo.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository: MeteoRepository = MeteoRepository(AppModule.provideGeocodingApi())

    private val _cities = MutableStateFlow<List<Ville>>(emptyList())
    val cities: StateFlow<List<Ville>> = _cities

    fun searchCity(query: String) {
        viewModelScope.launch {
            try {
                val result = repository.searchCities(query)
                _cities.value = result
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error fetching cities", e)
            }
        }
    }

    fun clearCities() {
        _cities.value = emptyList()
    }
}
