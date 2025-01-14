package com.example.appmeteo.data.api

import com.example.appmeteo.data.model.Ville
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {
    @GET("v1/search")
    suspend fun searchCity(@Query("name") name: String): GeocodingResponse
}

data class GeocodingResponse(
    val results: List<Ville>?
)
