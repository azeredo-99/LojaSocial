package com.example.lojasocial.ui.beneficiaries

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.lojasocial.models.Beneficiary
import com.example.lojasocial.repository.BeneficiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BeneficiariesViewModel @Inject constructor() : ViewModel() {

    var beneficiaries by mutableStateOf<List<Beneficiary>>(emptyList())
        private set

    fun load() {
        beneficiaries = BeneficiaryRepository.getAll()
    }

    fun addBeneficiary(beneficiary: Beneficiary) {
        BeneficiaryRepository.add(beneficiary)
        load()
    }

    fun updateBeneficiary(beneficiary: Beneficiary) {
        BeneficiaryRepository.update(beneficiary)
        load()
    }

    fun removeBeneficiary(id: String) {
        BeneficiaryRepository.remove(id)
        load()
    }
}
