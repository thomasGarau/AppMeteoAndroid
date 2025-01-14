package com.example.appmeteo.utils

import android.content.Context
import android.util.Log
import com.example.appmeteo.data.model.MeteoResponse
import com.example.appmeteo.data.model.Ville
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CacheManager(private val context: Context) {

    private val CACHE_FILE = "weather_cache.json"
    private val FAVORITES_FILE = "favorites.json"

    // Initialise Gson
    private val gson = GsonBuilder().create()

    private fun getFavoritesFile(): File {
        return File(context.filesDir, FAVORITES_FILE)
    }

    fun saveFavorites(favorites: List<Ville>) {
        val json = gson.toJson(favorites)
        getFavoritesFile().writeText(json)
    }

    fun loadFavorites(): List<Ville> {
        val file = getFavoritesFile()
        if (!file.exists()) return emptyList()
        val json = file.readText()
        val type = object : TypeToken<List<Ville>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    private fun getCacheFile(): File {
        return File(context.filesDir, CACHE_FILE)
    }

    fun saveWeatherData(cityName: String, data: MeteoResponse) {
        val cache = loadWeatherCache().toMutableMap()
        cache[cityName] = data

        try {
            val json = gson.toJson(cache)
            // Écriture dans le fichier
            getCacheFile().writeText(json)
        } catch (e: Exception) {
            Log.e("CacheManager", "Error during serialization: ${e.message}")
        }
    }

    fun getWeatherData(city: String): MeteoResponse? {
        // Chargez et désérialisez tout le cache
        val cache = loadWeatherCache()

        // Vérifiez si la ville est dans le cache
        val cachedData = cache[city]
        Log.d("CacheManager", "cached city : $cachedData")
        if (cachedData == null) {
            Log.d("CacheManager", "No cached data found for city: $city")
            return null
        }

        Log.d("CacheManager", "Cached data found for city: $city, data: $cachedData")
        return cachedData
    }

    private fun loadWeatherCache(): Map<String, MeteoResponse> {
        val file = getCacheFile()
        if (!file.exists()) {
            Log.d("CacheManager", "Cache file does not exist. Returning empty map.")
            return emptyMap()
        }

        val json = file.readText()
        Log.d("CacheManager", "Cache file content: $json")

        val type = object : TypeToken<Map<String, MeteoResponse>>() {}.type
        return try {
            val cache = gson.fromJson<Map<String, MeteoResponse>>(json, type)
            Log.d("CacheManager", "Successfully loaded cache: $cache")
            cache ?: emptyMap()
        } catch (e: Exception) {
            Log.e("CacheManager", "Error deserializing cache: ${e.message}", e)
            emptyMap()
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
