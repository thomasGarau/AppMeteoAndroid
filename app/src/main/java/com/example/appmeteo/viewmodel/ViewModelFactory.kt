package com.example.appmeteo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                FavoritesViewModel(context) as T
            }
            modelClass.isAssignableFrom(DetailsViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                DetailsViewModel(context) as T // Passez le contexte ici
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
