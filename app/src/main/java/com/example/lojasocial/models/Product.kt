package com.example.lojasocial.models

data class Product(
    val id: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val unit: String = "unidades",
    val category: String = "",
    val expireDate: Long? = null
)
