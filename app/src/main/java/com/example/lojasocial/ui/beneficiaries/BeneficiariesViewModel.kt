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
class BeneficiariesViewModel @Inject constructor() : ViewModel() {

    var beneficiaries by mutableStateOf<List<Beneficiary>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /* ---------------- LOAD ---------------- */
    fun load() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                beneficiaries = BeneficiaryRepository.getAll()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao carregar beneficiários"
            } finally {
                isLoading = false
            }
        }
    }

    /* ---------------- ADD ---------------- */
    fun addBeneficiary(beneficiary: Beneficiary) {
        viewModelScope.launch {
            try {
                BeneficiaryRepository.add(beneficiary)
                load()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao adicionar beneficiário"
            }
        }
    }

    /* ---------------- UPDATE ---------------- */
    fun updateBeneficiary(beneficiary: Beneficiary) {
        viewModelScope.launch {
            try {
                BeneficiaryRepository.update(beneficiary)
                load()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao atualizar beneficiário"
            }
        }
    }

    /* ---------------- REMOVE ---------------- */
    fun removeBeneficiary(id: String) {
        viewModelScope.launch {
            try {
                BeneficiaryRepository.remove(id)
                load()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao remover beneficiário"
            }
        }
    }
}
