package com.santiago.retrofit.ui.screen.CategoriesScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.santiago.retrofit.data.repositories.ProductRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class CategoriesUiState(
    val loading: Boolean = false,
    val categories: List<String>? = null,
    val error: String? = null
)

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProductRepository(application)
    private val _uiState = MutableLiveData(CategoriesUiState(loading = true))
    val uiState: LiveData<CategoriesUiState> = _uiState

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                repository.products.collectLatest { products ->
                    val categories = products.map { it.category }.distinct().sorted()
                    _uiState.value = CategoriesUiState(
                        loading = false,
                        categories = categories
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CategoriesUiState(
                    loading = false,
                    error = "Error al cargar categorías: ${e.message}"
                )
            }
        }
    }
} 