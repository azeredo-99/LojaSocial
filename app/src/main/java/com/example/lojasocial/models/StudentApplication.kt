package com.example.lojasocial.models

import com.google.firebase.Timestamp

data class StudentApplication(
    val id: String = "",
    val userId: String = "",
    val nome: String = "",
    val numeroAluno: String = "",
    val curso: String = "",
    val email: String = "",
    val telemovel: String = "",
    val mensagem: String = "",
    val estado: String = "PENDENTE",
    val createdAt: Timestamp? = null
)
