package com.example.lojasocial.models

import com.google.firebase.Timestamp

data class Alert(
    val id: String = "",
    val type: String = "",
    val entityId: String = "",
    val title: String = "",
    val message: String = "",
    val severity: String = "INFO",
    val createdAt: Timestamp? = null,
    val resolved: Boolean = false,
    val resolvedAt: Timestamp? = null
)
