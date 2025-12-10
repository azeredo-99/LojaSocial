package com.example.lojasocial.ui.login

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.lojasocial.models.Employee
import com.example.lojasocial.repository.AuthRepository
import com.example.lojasocial.repository.ResultWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")

    var uiState by mutableStateOf<ResultWrapper<*>?>(null)

    fun login() = viewModelScope.launch {
        uiState = ResultWrapper.Loading
        uiState = repo.login(email, password)
    }

    fun register() = viewModelScope.launch {
        uiState = ResultWrapper.Loading
        val employee = Employee(name = name, email = email)
        uiState = repo.register(employee, password)
    }

    fun recover() = viewModelScope.launch {
        uiState = ResultWrapper.Loading
        uiState = repo.recover(email)
    }
}
