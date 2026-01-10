package com.example.lojasocial.models

data class Schedule(
    val id: String = "",
    val beneficiaryName: String = "",
    val studentNumber: String = "",
    val course: String = "",

    // Agendamento
    val date: String = "",     // ex: 15/01/2026
    val time: String = "",     // ex: 10:30

    // Observações
    val notes: String = ""
)
