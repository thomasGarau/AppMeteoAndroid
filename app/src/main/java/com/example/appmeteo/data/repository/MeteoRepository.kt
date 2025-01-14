package com.example.appmeteo.data.repository

import com.example.appmeteo.data.api.GeocodingApi
import com.example.appmeteo.data.model.Ville

class MeteoRepository(private val geocodingApi: GeocodingApi) {
    suspend fun searchCities(query: String): List<Ville> {
        val response = geocodingApi.searchCity(query)
        return response.results ?: emptyList()
    }
}
