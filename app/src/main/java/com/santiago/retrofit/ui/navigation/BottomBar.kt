package com.santiago.retrofit.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Inicio") },
            selected = currentRoute == "lista",
            onClick = {
                if (currentRoute != "lista") {
                    navController.navigate("lista") {
                        popUpTo("lista") { inclusive = true }
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Categories") },
            label = { Text("Categorías") },
            selected = currentRoute == "categories",
            onClick = {
                if (currentRoute != "categories") {
                    navController.navigate("categories") {
                        popUpTo("lista")
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Buscar") },
            selected = currentRoute == "search",
            onClick = {
                if (currentRoute != "search") {
                    navController.navigate("search") {
                        popUpTo("lista")
                    }
                }
            }
        )
    }
} 