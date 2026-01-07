package com.example.lojasocial.ui.beneficiaries

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.models.Beneficiary
import com.example.lojasocial.repository.BeneficiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeneficiaryDetailViewModel @Inject constructor() : ViewModel() {

    var beneficiary by mutableStateOf<Beneficiary?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun load(id: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                beneficiary = BeneficiaryRepository.getById(id)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao carregar beneficiário"
            } finally {
                isLoading = false
            }
        }
    }

    fun remove(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                BeneficiaryRepository.remove(id)
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao remover beneficiário"
            }
        }
    }
}
