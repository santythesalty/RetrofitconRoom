package com.santiago.retrofit.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationElement {
    @Serializable
    object List : NavigationElement()
    
    @Serializable
    data class Detail(val id: Int) : NavigationElement()
    
    @Serializable
    object Categories : NavigationElement()
    
    @Serializable
    data class Category(val name: String) : NavigationElement()
    
    @Serializable
    object Search : NavigationElement()
}

fun NavigationElement.route(): String {
    return when (this) {
        is NavigationElement.List -> "lista"
        is NavigationElement.Detail -> "detail/${id}"
        is NavigationElement.Categories -> "categories"
        is NavigationElement.Category -> "category/${name}"
        is NavigationElement.Search -> "search"
    }
} 