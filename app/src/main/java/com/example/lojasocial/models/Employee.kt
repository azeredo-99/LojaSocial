package com.example.lojasocial.models

data class Employee(
    val id: String? = null,
    val name: String? = "",
    val email: String? = "",
    val role: String? = "staff",
    val state: String? = "active"
)
