package com.example.appmeteo.utils

import android.content.Context
import android.util.Log
import com.example.appmeteo.data.model.MeteoResponse
import com.example.appmeteo.data.model.Ville
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CacheManager(private val context: Context) {

    private val CACHE_FILE = "weather_cache.json"
    private val FAVORITES_FILE = "favorites.json"

    // Initialise Gson avec un adaptateur personnalisé pour MeteoResponse
    private val gson = GsonBuilder()
        .registerTypeAdapter(MeteoResponse::class.java, MeteoResponseAdapter())
        .create()

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
        cache[cityName] = Pair(getCurrentDate(), data)

        try {
            val json = gson.toJson(cache)
            // Écriture dans le fichier
            getCacheFile().writeText(json)
        } catch (e: Exception) {
            Log.e("CacheManager", "Error during serialization: ${e.message}")
        }
    }


    fun getWeatherData(cityName: String): MeteoResponse? {
        val cache = loadWeatherCache()
        val (date, data) = cache[cityName] ?: return null
        return if (date == getCurrentDate()) data else null
    }

    private fun loadWeatherCache(): Map<String, Pair<String, MeteoResponse>> {
        val file = getCacheFile()
        if (!file.exists()) {
            return emptyMap()
        }
        val json = file.readText()
        val type = object : TypeToken<Map<String, Pair<String, MeteoResponse>>>() {}.type
        val cache = gson.fromJson<Map<String, Pair<String, MeteoResponse>>>(json, type) ?: emptyMap()
        return cache
    }


    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
