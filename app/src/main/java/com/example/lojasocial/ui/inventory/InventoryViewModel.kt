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

    var selectedProduct by mutableStateOf<Product?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun load() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                products = ProductRepository.getAll()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao carregar produtos"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadById(id: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            selectedProduct = null
            try {
                selectedProduct = ProductRepository.getById(id)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao carregar produto"
            } finally {
                isLoading = false
            }
        }
    }

    fun update(product: Product, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                ProductRepository.update(product)
                load()
                onDone()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao atualizar produto"
            } finally {
                isLoading = false
            }
        }
    }

    fun remove(id: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                ProductRepository.remove(id)
                load()
                onDone()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao remover produto"
            } finally {
                isLoading = false
            }
        }
    }
}
