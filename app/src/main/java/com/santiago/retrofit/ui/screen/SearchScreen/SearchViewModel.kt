package com.santiago.retrofit.ui.screen.SearchScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.santiago.retrofit.data.model.Product
import com.santiago.retrofit.data.repositories.ProductRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class SearchUiState(
    val loading: Boolean = false,
    val products: List<Product>? = null,
    val error: String? = null
)

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProductRepository(application)
    private val _uiState = MutableLiveData(SearchUiState())
    val uiState: LiveData<SearchUiState> = _uiState

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            _uiState.value = SearchUiState(products = emptyList())
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = SearchUiState(loading = true)
                repository.searchProducts(query).collectLatest { products ->
                    _uiState.value = SearchUiState(
                        loading = false,
                        products = products
                    )
                }
            } catch (e: Exception) {
                _uiState.value = SearchUiState(
                    loading = false,
                    error = "Error al buscar productos: ${e.message}"
                )
            }
        }
    }
} 