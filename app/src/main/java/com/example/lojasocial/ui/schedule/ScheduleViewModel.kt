package com.example.lojasocial.ui.schedule

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.models.Schedule
import com.example.lojasocial.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor() : ViewModel() {

    var schedules by mutableStateOf<List<Schedule>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    /* ---------------- LOAD ---------------- */
    fun load() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                schedules = ScheduleRepository.getAll()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao carregar agendamentos"
            } finally {
                isLoading = false
            }
        }
    }

    /* ---------------- ADD ---------------- */
    fun add(schedule: Schedule) {
        viewModelScope.launch {
            try {
                ScheduleRepository.add(schedule)
                load()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao adicionar agendamento"
            }
        }
    }

    /* ---------------- UPDATE ---------------- */
    fun update(schedule: Schedule) {
        viewModelScope.launch {
            try {
                ScheduleRepository.update(schedule)
                load()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao atualizar agendamento"
            }
        }
    }

    /* ---------------- REMOVE ---------------- */
    fun remove(id: String) {
        viewModelScope.launch {
            try {
                ScheduleRepository.remove(id)
                load()
            } catch (e: Exception) {
                error = e.message ?: "Erro ao remover agendamento"
            }
        }
    }
}
