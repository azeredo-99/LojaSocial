package com.example.lojasocial.models

data class Product(
    val id: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val unit: String = "",
    val category: String = "",
    val expireDate: Long? = null
) {

    fun isExpired(): Boolean {
        return expireDate?.let { it < System.currentTimeMillis() } ?: false
    }


    fun isExpiringSoon(daysThreshold: Int = 7): Boolean {
        return expireDate?.let {
            val daysUntilExpiry = (it - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)
            daysUntilExpiry in 0..daysThreshold
        } ?: false
    }


    fun daysUntilExpiry(): Long? {
        return expireDate?.let {
            (it - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)
        }
    }
}
