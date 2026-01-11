package com.example.lojasocial.models

data class Donation(
    val id: String = "",

    val donorName: String = "",
    val donorContact: String = "",

    val date: String = "",

    val items: List<Product> = emptyList(),

    val notes: String = "",

    val received: Boolean = true
)
