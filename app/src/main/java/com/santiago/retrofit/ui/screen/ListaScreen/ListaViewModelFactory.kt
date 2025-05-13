package com.santiago.retrofit.ui.screen.ListaScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListaViewModelFactory(
    private val application: Application,
    private val category: String? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListaViewModel(application, category) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 