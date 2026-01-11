package com.example.lojasocial.repository

import android.annotation.SuppressLint
import com.example.lojasocial.models.Delivery
import com.example.lojasocial.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object DeliveryRepository {

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private const val COLLECTION = "deliveries"

    /* -------- GET ALL (HISTORY GLOBAL) -------- */
    suspend fun getAll(): List<Delivery> {
        return db.collection(COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                try {
                    val data = doc.data ?: return@mapNotNull null

                    // Mapear os items manualmente
                    val itemsList = (data["items"] as? List<*>)?.mapNotNull { item ->
                        (item as? Map<*, *>)?.let { map ->
                            Product(
                                id = map["id"] as? String ?: "",
                                name = map["name"] as? String ?: "",
                                quantity = (map["quantity"] as? Long)?.toInt() ?: 0,
                                unit = map["unit"] as? String ?: "unidades",
                                category = map["category"] as? String ?: "",
                                expireDate = map["expireDate"] as? Long
                            )
                        }
                    } ?: emptyList()

                    Delivery(
                        id = doc.id,
                        beneficiaryName = data["beneficiaryName"] as? String ?: "",
                        studentNumber = data["studentNumber"] as? String ?: "",
                        course = data["course"] as? String ?: "",
                        date = data["date"] as? String ?: "",
                        state = data["state"] as? Boolean ?: false,
                        items = itemsList,
                        notes = data["notes"] as? String ?: ""
                    )
                } catch (e: Exception) {
                    null
                }
            }
    }

    /* -------- GET BY ID -------- */
    suspend fun getById(id: String): Delivery? {
        return try {
            val doc = db.collection(COLLECTION)
                .document(id)
                .get()
                .await()

            if (!doc.exists()) return null

            val data = doc.data ?: return null

            val itemsList = (data["items"] as? List<*>)?.mapNotNull { item ->
                (item as? Map<*, *>)?.let { map ->
                    Product(
                        id = map["id"] as? String ?: "",
                        name = map["name"] as? String ?: "",
                        quantity = (map["quantity"] as? Long)?.toInt() ?: 0,
                        unit = map["unit"] as? String ?: "unidades",
                        category = map["category"] as? String ?: "",
                        expireDate = map["expireDate"] as? Long
                    )
                }
            } ?: emptyList()

            Delivery(
                id = doc.id,
                beneficiaryName = data["beneficiaryName"] as? String ?: "",
                studentNumber = data["studentNumber"] as? String ?: "",
                course = data["course"] as? String ?: "",
                date = data["date"] as? String ?: "",
                state = data["state"] as? Boolean ?: false,
                items = itemsList,
                notes = data["notes"] as? String ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }

    /* -------- GET BY STUDENT NUMBER -------- */
    suspend fun getByStudentNumber(studentNumber: String): List<Delivery> {
        return db.collection(COLLECTION)
            .whereEqualTo("studentNumber", studentNumber)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                try {
                    val data = doc.data ?: return@mapNotNull null

                    val itemsList = (data["items"] as? List<*>)?.mapNotNull { item ->
                        (item as? Map<*, *>)?.let { map ->
                            Product(
                                id = map["id"] as? String ?: "",
                                name = map["name"] as? String ?: "",
                                quantity = (map["quantity"] as? Long)?.toInt() ?: 0,
                                unit = map["unit"] as? String ?: "unidades",
                                category = map["category"] as? String ?: "",
                                expireDate = map["expireDate"] as? Long
                            )
                        }
                    } ?: emptyList()

                    Delivery(
                        id = doc.id,
                        beneficiaryName = data["beneficiaryName"] as? String ?: "",
                        studentNumber = data["studentNumber"] as? String ?: "",
                        course = data["course"] as? String ?: "",
                        date = data["date"] as? String ?: "",
                        state = data["state"] as? Boolean ?: false,
                        items = itemsList,
                        notes = data["notes"] as? String ?: ""
                    )
                } catch (e: Exception) {
                    null
                }
            }
    }

    /* -------- ADD -------- */
    suspend fun add(delivery: Delivery): String {
        val docRef = db.collection(COLLECTION)
            .add(
                hashMapOf(
                    "beneficiaryName" to delivery.beneficiaryName,
                    "studentNumber" to delivery.studentNumber,
                    "course" to delivery.course,
                    "date" to delivery.date,
                    "state" to delivery.state,
                    "items" to delivery.items.map { product ->
                        hashMapOf(
                            "id" to product.id,
                            "name" to product.name,
                            "quantity" to product.quantity,
                            "unit" to product.unit,
                            "category" to product.category,
                            "expireDate" to product.expireDate
                        )
                    },
                    "notes" to delivery.notes
                )
            )
            .await()

        return docRef.id
    }

    /* -------- UPDATE -------- */
    suspend fun update(delivery: Delivery) {
        db.collection(COLLECTION)
            .document(delivery.id)
            .set(
                hashMapOf(
                    "beneficiaryName" to delivery.beneficiaryName,
                    "studentNumber" to delivery.studentNumber,
                    "course" to delivery.course,
                    "date" to delivery.date,
                    "state" to delivery.state,
                    "items" to delivery.items.map { product ->
                        hashMapOf(
                            "id" to product.id,
                            "name" to product.name,
                            "quantity" to product.quantity,
                            "unit" to product.unit,
                            "category" to product.category,
                            "expireDate" to product.expireDate
                        )
                    },
                    "notes" to delivery.notes
                )
            )
            .await()
    }

    /* -------- UPDATE STATE -------- */
    suspend fun updateState(id: String, state: Boolean) {
        db.collection(COLLECTION)
            .document(id)
            .update("state", state)
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
