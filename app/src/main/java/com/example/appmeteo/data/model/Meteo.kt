package com.example.appmeteo.data.model

data class MeteoResponse(
    val hourly: HourlyData
)

data class HourlyData(
    val temperature_2m: List<Double>,
    val relative_humidity_2m: List<Double>,
    val apparent_temperature: List<Double>,
    val rain: List<Double>,
    val wind_speed_10m: List<Double>
)
