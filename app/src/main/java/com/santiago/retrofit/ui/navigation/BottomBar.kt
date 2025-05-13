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
            selected = currentRoute == NavigationElement.List.route(),
            onClick = {
                if (currentRoute != NavigationElement.List.route()) {
                    navController.navigate(NavigationElement.List.route()) {
                        popUpTo(NavigationElement.List.route()) { inclusive = true }
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Categories") },
            label = { Text("Categorías") },
            selected = currentRoute == NavigationElement.Categories.route(),
            onClick = {
                if (currentRoute != NavigationElement.Categories.route()) {
                    navController.navigate(NavigationElement.Categories.route()) {
                        popUpTo(NavigationElement.List.route())
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Buscar") },
            selected = currentRoute == NavigationElement.Search.route(),
            onClick = {
                if (currentRoute != NavigationElement.Search.route()) {
                    navController.navigate(NavigationElement.Search.route()) {
                        popUpTo(NavigationElement.List.route())
                    }
                }
            }
        )
    }
} 