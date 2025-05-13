package com.santiago.retrofit.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.santiago.retrofit.ui.screen.CategoriesScreen.CategoriesScreen
import com.santiago.retrofit.ui.screen.DetailScreen.DetailScreen
import com.santiago.retrofit.ui.screen.ListaScreen.ListaScreen
import com.santiago.retrofit.ui.screen.SearchScreen.SearchScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
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
                    onProductClick = { productId ->
                        navController.navigate("detail/$productId")
                    }
                )
            }
            composable("detail/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                if (productId != null) {
                    DetailScreen(
                        productId = productId,
                        onBackClick = {
                            if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                            } else {
                                navController.navigate("lista") {
                                    popUpTo("lista") { inclusive = true }
                                }
                            }
                        }
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
                        onProductClick = { productId ->
                            navController.navigate("detail/$productId")
                        }
                    )
                }
            }
            composable("search") {
                SearchScreen(
                    onProductClick = { productId ->
                        navController.navigate("detail/$productId")
                    }
                )
            }
        }
    }
} 