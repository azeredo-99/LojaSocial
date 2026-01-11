package com.example.lojasocial.ui.donations

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.models.Donation
import com.example.lojasocial.repository.DonationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonationsViewModel @Inject constructor() : ViewModel() {

    var donations by mutableStateOf<List<Donation>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    /* ---------- LOAD ---------- */
    fun load() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                donations = DonationRepository.getAll()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao carregar doações"
            } finally {
                isLoading = false
            }
        }
    }

    /* ---------- ADD ---------- */
    fun addDonation(donation: Donation) {
        viewModelScope.launch {
            try {
                DonationRepository.add(donation)
                load()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao adicionar doação"
            }
        }
    }

    /* ---------- UPDATE ---------- */
    fun updateDonation(donation: Donation) {
        viewModelScope.launch {
            try {
                DonationRepository.update(donation)
                load()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao atualizar doação"
            }
        }
    }

    /* ---------- TOGGLE RECEIVED ---------- */
    fun toggleReceived(donation: Donation) {
        viewModelScope.launch {
            try {
                DonationRepository.updateReceived(
                    donation.id,
                    !donation.received
                )
                load()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao atualizar estado da doação"
            }
        }
    }

    /* ---------- REMOVE ---------- */
    fun removeDonation(id: String) {
        viewModelScope.launch {
            try {
                DonationRepository.remove(id)
                load()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao remover doação"
            }
        }
    }
}
