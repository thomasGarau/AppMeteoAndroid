package com.example.appmeteo

import com.example.appmeteo.data.api.GeocodingApi
import com.example.appmeteo.data.api.MeteoApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {
    fun provideGeocodingApi(): GeocodingApi {
        return Retrofit.Builder()
            .baseUrl("https://geocoding-api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingApi::class.java)
    }

    fun provideMeteoApi(): MeteoApi {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MeteoApi::class.java)
    }

}
