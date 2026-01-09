package com.example.lojasocial.ui.inventory

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.models.Product
import com.example.lojasocial.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor() : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var expiredProducts by mutableStateOf<List<Product>>(emptyList())
        private set

    var expiringSoonProducts by mutableStateOf<List<Product>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun load() {
        viewModelScope.launch {
            isLoading = true
            products = ProductRepository.getAll()
            expiredProducts = ProductRepository.getExpired()
            expiringSoonProducts = ProductRepository.getExpiringSoon()
            isLoading = false
        }
    }

    fun loadByCategory(category: String) {
        viewModelScope.launch {
            isLoading = true
            products = ProductRepository.getByCategory(category)
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

    fun removeExpiredProducts() {
        viewModelScope.launch {
            ProductRepository.removeExpired()
            load()
        }
    }

    // Retorna o número de produtos vencidos
    fun getExpiredCount(): Int = expiredProducts.size

    // Retorna o número de produtos próximos do vencimento
    fun getExpiringSoonCount(): Int = expiringSoonProducts.size

    // Verifica se há alertas de validade
    fun hasExpiryAlerts(): Boolean =
        expiredProducts.isNotEmpty() || expiringSoonProducts.isNotEmpty()
}