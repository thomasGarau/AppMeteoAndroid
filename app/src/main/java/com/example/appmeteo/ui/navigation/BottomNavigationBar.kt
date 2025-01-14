package com.example.appmeteo.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.appmeteo.R

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            selected = selectedTab == "home",
            onClick = { onTabSelected("home") },
            label = { Text("Home") },
            icon = { Icon(painter = painterResource(R.drawable.ic_home), contentDescription = "Home") }
        )
        NavigationBarItem(
            selected = selectedTab == "search",
            onClick = { onTabSelected("search") },
            label = { Text("Search") },
            icon = { Icon(painter = painterResource(R.drawable.ic_search), contentDescription = "Search") }
        )
    }
}
