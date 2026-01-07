package com.example.lojasocial.ui.products

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.models.Product
import com.example.lojasocial.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor() : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun load() {
        viewModelScope.launch {
            isLoading = true
            products = ProductRepository.getAll()
            isLoading = false
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            ProductRepository.add(product)
            load()
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            ProductRepository.update(product)
            load()
        }
    }

    fun removeProduct(id: String) {
        viewModelScope.launch {
            ProductRepository.remove(id)
            load()
        }
    }
}
