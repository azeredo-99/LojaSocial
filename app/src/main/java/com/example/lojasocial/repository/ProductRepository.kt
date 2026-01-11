package com.example.lojasocial.repository

import android.annotation.SuppressLint
import com.example.lojasocial.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object ProductRepository {

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private const val COLLECTION = "products"

    /* ---------- GET ALL ---------- */
    suspend fun getAll(): List<Product> {
        return db.collection(COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(Product::class.java)
                    ?.copy(id = doc.id)
            }
    }

    /* ---------- GET BY CATEGORY ---------- */
    suspend fun getByCategory(category: String): List<Product> {
        return getAll().filter { it.category.equals(category, ignoreCase = true) }
    }

    /* ---------- GET EXPIRED PRODUCTS ---------- */
    suspend fun getExpired(): List<Product> {
        return getAll().filter { it.isExpired() }
    }

    /* ---------- GET EXPIRING SOON ---------- */
    suspend fun getExpiringSoon(daysThreshold: Int = 7): List<Product> {
        return getAll().filter { it.isExpiringSoon(daysThreshold) }
    }

    /* ---------- ADD ---------- */
    suspend fun add(product: Product) {
        db.collection(COLLECTION)
            .add(product.copy(id = ""))
            .await()
    }

    /* ---------- UPDATE ---------- */
    suspend fun update(product: Product) {
        if (product.id.isBlank()) return

        db.collection(COLLECTION)
            .document(product.id)
            .set(product.copy(id = ""))
            .await()
    }

    /* ---------- REMOVE ---------- */
    suspend fun remove(id: String) {
        db.collection(COLLECTION)
            .document(id)
            .delete()
            .await()
    }

    /* ---------- REMOVE EXPIRED ---------- */
    suspend fun removeExpired() {
        val expiredProducts = getExpired()
        expiredProducts.forEach { product ->
            remove(product.id)
        }
    }
}
