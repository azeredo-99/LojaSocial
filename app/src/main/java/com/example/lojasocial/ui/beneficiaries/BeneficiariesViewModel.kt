package com.example.lojasocial.ui.beneficiaries

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.models.Beneficiary
import com.example.lojasocial.models.StudentApplication
import com.example.lojasocial.repository.BeneficiaryRepository
import com.example.lojasocial.repository.ResultWrapper
import com.example.lojasocial.repository.StudentApplicationsAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeneficiariesViewModel @Inject constructor() : ViewModel() {

    var beneficiaries by mutableStateOf<List<Beneficiary>>(emptyList())
        private set

    var pendingApplications by mutableStateOf<List<StudentApplication>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /* ---------------- LOAD BENEFICIÁRIOS ---------------- */
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

    /* ---------------- LOAD CANDIDATURAS ---------------- */
    fun loadPendingApplications() {
        viewModelScope.launch {
            when (val res = StudentApplicationsAdminRepository.getPending()) {
                is ResultWrapper.Success -> pendingApplications = res.data
                is ResultWrapper.Error -> {

                }
                else -> Unit
            }
        }
    }

    /* ---------------- ACEITAR CANDIDATURA ---------------- */
    fun acceptApplication(app: StudentApplication) {
        viewModelScope.launch {

            val beneficiary = Beneficiary(
                name = app.nome.trim(),
                studentNumber = app.numeroAluno.trim(),
                course = app.curso.trim(),
                active = true
            )

            try {
                BeneficiaryRepository.add(beneficiary)
                StudentApplicationsAdminRepository.markAccepted(app.id)
                loadPendingApplications()
                load()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Erro ao aceitar candidatura"
            }
        }
    }

    /* ---------------- REJEITAR CANDIDATURA ---------------- */
    fun rejectApplication(appId: String) {
        viewModelScope.launch {
            when (val res = StudentApplicationsAdminRepository.markRejected(appId)) {
                is ResultWrapper.Success -> loadPendingApplications()
                is ResultWrapper.Error -> errorMessage = res.exception.message ?: "Erro ao rejeitar candidatura"
                else -> Unit
            }
        }
    }

    /* ---------------- ADD/UPDATE/REMOVE BENEFICIÁRIO ---------------- */
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
