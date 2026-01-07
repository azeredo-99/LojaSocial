package com.example.lojasocial.ui.deliveries

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.models.Delivery
import com.example.lojasocial.repository.DeliveryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveriesViewModel @Inject constructor() : ViewModel() {

    var deliveries by mutableStateOf<List<Delivery>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun load() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                deliveries = DeliveryRepository.getAll()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao carregar entregas"
            } finally {
                isLoading = false
            }
        }
    }

    fun addDelivery(delivery: Delivery) {
        viewModelScope.launch {
            try {
                DeliveryRepository.add(delivery)
                load()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao adicionar entrega"
            }
        }
    }

    fun removeDelivery(id: String) {
        viewModelScope.launch {
            try {
                DeliveryRepository.remove(id)
                load()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao remover entrega"
            }
        }
    }
}
