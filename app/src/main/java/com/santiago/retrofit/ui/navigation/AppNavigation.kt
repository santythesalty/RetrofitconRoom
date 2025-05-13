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
            startDestination = NavigationElement.List.route(),
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavigationElement.List.route()) {
                ListaScreen(
                    onProductClick = { productId ->
                        navController.navigate(NavigationElement.Detail(productId).route())
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
                                navController.navigate(NavigationElement.List.route()) {
                                    popUpTo(NavigationElement.List.route()) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
            composable(NavigationElement.Categories.route()) {
                CategoriesScreen(
                    onCategoryClick = { category ->
                        navController.navigate(NavigationElement.Category(category).route())
                    }
                )
            }
            composable("category/{name}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("name")
                if (category != null) {
                    ListaScreen(
                        category = category,
                        onProductClick = { productId ->
                            navController.navigate(NavigationElement.Detail(productId).route())
                        }
                    )
                }
            }
            composable(NavigationElement.Search.route()) {
                SearchScreen(
                    onProductClick = { productId ->
                        navController.navigate(NavigationElement.Detail(productId).route())
                    }
                )
            }
        }
    }
} 