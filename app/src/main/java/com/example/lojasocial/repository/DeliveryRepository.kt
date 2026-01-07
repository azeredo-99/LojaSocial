package com.example.lojasocial.repository

import android.annotation.SuppressLint
import com.example.lojasocial.models.Delivery
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object DeliveryRepository {

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private const val COLLECTION = "deliveries"

    /* -------- GET ALL (HISTÓRICO GLOBAL) -------- */
    suspend fun getAll(): List<Delivery> {
        return db.collection(COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(Delivery::class.java)
                    ?.copy(id = doc.id)
            }
    }

    /* -------- ADD -------- */
    suspend fun add(delivery: Delivery) {
        db.collection(COLLECTION)
            .add(delivery.copy(id = ""))
            .await()
    }

    /* -------- REMOVE -------- */
    suspend fun remove(id: String) {
        db.collection(COLLECTION)
            .document(id)
            .delete()
            .await()
    }
}
