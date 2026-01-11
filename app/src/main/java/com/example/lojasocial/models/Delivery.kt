package com.example.lojasocial.models

data class Delivery(
    val id: String = "",
    val beneficiaryName: String = "",
    val studentNumber: String = "",
    val course: String = "",
    val date: String = "",
    val state: Boolean,
    val items: List<Product> = emptyList(),
    val notes: String = ""
)
