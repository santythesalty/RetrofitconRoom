package com.santiago.retrofit.ui.screen.DetailScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.santiago.retrofit.data.model.Product
import com.santiago.retrofit.data.repositories.ProductRepository
import kotlinx.coroutines.launch

class DetailViewModel(application: Application, private val productId: Int) : AndroidViewModel(application) {
    private val repository = ProductRepository(application)
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState(loading = true)
                val product = repository.getProductById(productId)
                if (product != null) {
                    _uiState.value = UiState(product = product)
                } else {
                    _uiState.value = UiState(error = "Producto no encontrado")
                }
            } catch (e: Exception) {
                _uiState.value = UiState(error = e.message)
            }
        }
    }
}

data class UiState(
    val loading: Boolean = false,
    val product: Product? = null,
    val error: String? = null
) 