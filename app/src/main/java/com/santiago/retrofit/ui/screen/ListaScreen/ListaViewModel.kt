package com.santiago.retrofit.ui.screen.ListaScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.santiago.retrofit.data.model.Product
import com.santiago.retrofit.data.repositories.ProductRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class UiState(
    val loading: Boolean = false,
    val products: List<Product>? = null,
    val navigateTo: Int? = null,
    val error: String? = null,
    val isLocalData: Boolean = false
)

class ListaViewModel(application: Application, private val category: String? = null) : AndroidViewModel(application) {
    private val repository = ProductRepository(application)
    private val _uiState = MutableLiveData(UiState(loading = true))
    val uiState: LiveData<UiState> = _uiState

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                // Primero intentamos obtener datos locales
                val hasLocalData = repository.hasLocalData()
                if (hasLocalData) {
                    Log.d("ListaViewModel", "Usando datos locales de Room")
                } else {
                    Log.d("ListaViewModel", "No hay datos locales, obteniendo de la API")
                }

                // Siempre refrescamos los datos de la API
                repository.refreshProducts()
                
                // Observamos los cambios en la base de datos local
                val flow = if (category != null) {
                    repository.getProductsByCategory(category)
                } else {
                    repository.products
                }
                
                flow.collectLatest { products ->
                    _uiState.value = _uiState.value?.copy(
                        products = products,
                        loading = false,
                        error = null,
                        isLocalData = true
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    loading = false,
                    error = "Error al cargar productos: ${e.message}"
                )
            }
        }
    }

    fun navigateToDetail(productId: Int) {
        _uiState.value = _uiState.value?.copy(navigateTo = productId)
    }

    fun navigateToDetailDone() {
        _uiState.value = _uiState.value?.copy(navigateTo = null)
    }
} 