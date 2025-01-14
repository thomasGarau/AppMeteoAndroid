package com.example.appmeteo.data.api

import com.example.appmeteo.data.model.MeteoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MeteoApi {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,apparent_temperature,rain,wind_speed_10m",
        @Query("models") models: String = "meteofrance_seamless"
    ): MeteoResponse
}
