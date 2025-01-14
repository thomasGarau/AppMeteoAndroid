package com.example.appmeteo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmeteo.ui.home.HomeScreen
import com.example.appmeteo.ui.navigation.BottomNavigationBar
import com.example.appmeteo.ui.search.SearchScreen
import com.example.appmeteo.ui.theme.AppMeteoTheme
import com.example.appmeteo.utils.CacheManager
import com.example.appmeteo.viewmodel.DetailsViewModel
import com.example.appmeteo.viewmodel.FavoritesViewModel
import com.example.appmeteo.viewmodel.*
import com.example.appmeteo.viewmodel.SearchViewModel

class MainActivity : ComponentActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisation du launcher pour demander les permissions
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                // Permissions accordées
            } else {
                // Permissions refusées, affichez un message ou désactivez la fonctionnalité
            }
        }

        // Vérifiez et demandez les permissions si nécessaire
        checkAndRequestPermissions()
        val viewModelFactory = ViewModelFactory(applicationContext)
        setContent {
            AppMeteoTheme {
                val favoritesViewModel: FavoritesViewModel = viewModel(factory = viewModelFactory)
                val searchViewModel: SearchViewModel = viewModel()
                val detailsViewModel: DetailsViewModel = viewModel(factory = viewModelFactory)

                var selectedTab by remember { mutableStateOf("home") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                ) { innerPadding ->
                    when (selectedTab) {
                        "home" -> HomeScreen(
                            modifier = Modifier.padding(innerPadding),
                            favoritesViewModel = favoritesViewModel,
                            detailsViewModel = detailsViewModel // Fourniture du detailsViewModel
                        )
                        "search" -> SearchScreen(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = searchViewModel,
                            favoritesViewModel = favoritesViewModel,
                            detailsViewModel = detailsViewModel // Fourniture du detailsViewModel
                        )
                    }
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationGranted && !coarseLocationGranted) {
            // Demande des permissions
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}
