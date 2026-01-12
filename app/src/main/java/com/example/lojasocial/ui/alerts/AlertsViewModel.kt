package com.example.lojasocial.ui.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.models.Alert
import com.example.lojasocial.repository.AlertRepository
import com.example.lojasocial.repository.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlertsViewModel : ViewModel() {

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts: StateFlow<List<Alert>> = _alerts.asStateFlow()

    private val _status = MutableStateFlow<ResultWrapper<Unit>?>(null)
    val status: StateFlow<ResultWrapper<Unit>?> = _status.asStateFlow()

    fun loadActive() {
        viewModelScope.launch {
            _alerts.value = AlertRepository.getActive()
        }
    }

    fun generate() {
        viewModelScope.launch {
            _status.value = ResultWrapper.Loading

            when (val res = AlertRepository.generateAllAlerts()) {
                is ResultWrapper.Success -> _status.value = ResultWrapper.Success(Unit)
                is ResultWrapper.Error -> _status.value = ResultWrapper.Error(res.exception)
                ResultWrapper.Loading -> _status.value = ResultWrapper.Loading
            }

            loadActive()
        }
    }

    fun resolve(alertId: String) {
        viewModelScope.launch {
            _status.value = ResultWrapper.Loading
            try {
                AlertRepository.resolve(alertId)
                _status.value = ResultWrapper.Success(Unit)
            } catch (e: Exception) {
                _status.value = ResultWrapper.Error(e)
            }
            loadActive()
        }
    }

    fun clearStatus() {
        _status.value = null
    }
}
