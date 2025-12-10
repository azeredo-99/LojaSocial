package com.example.lojasocial.repository

import com.example.lojasocial.models.Employee

interface AuthRepository {
    suspend fun login(email: String, password: String): ResultWrapper<Employee?>
    suspend fun register(employee: Employee, password: String): ResultWrapper<Boolean>
    suspend fun recover(email: String): ResultWrapper<Boolean>
}
