package com.santiago.retrofit.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.santiago.retrofit.ui.navigation.BottomBar
import com.santiago.retrofit.ui.screen.DetailScreen.DetailScreen
import com.santiago.retrofit.ui.screen.ListaScreen.ListaScreen
import com.santiago.retrofit.ui.screen.CategoriesScreen.CategoriesScreen
import com.santiago.retrofit.ui.screen.SearchScreen.SearchScreen

@Composable
fun Navegacion() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "lista",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("lista") {
                ListaScreen(
                    onProductClick = { id ->
                        navController.navigate("detail/$id")
                    }
                )
            }
            composable("detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                if (id != null) {
                    DetailScreen(
                        productId = id,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
            composable("categories") {
                CategoriesScreen(
                    onCategoryClick = { category ->
                        navController.navigate("category/$category")
                    }
                )
            }
            composable("category/{category}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")
                if (category != null) {
                    ListaScreen(
                        category = category,
                        onProductClick = { id ->
                            navController.navigate("detail/$id")
                        }
                    )
                }
            }
            composable("search") {
                SearchScreen(
                    onProductClick = { id ->
                        navController.navigate("detail/$id")
                    }
                )
            }
        }
    }
} 