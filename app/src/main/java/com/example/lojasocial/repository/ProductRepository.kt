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
}
