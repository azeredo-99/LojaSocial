package com.example.lojasocial.repository

import android.annotation.SuppressLint
import com.example.lojasocial.models.Beneficiary
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object BeneficiaryRepository {

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private const val COLLECTION = "beneficiaries"

    /* ---------------- GET ALL ---------------- */
    suspend fun getAll(): List<Beneficiary> {
        return db.collection(COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(Beneficiary::class.java)
                    ?.copy(id = doc.id)
            }
    }

    /* ---------------- GET BY ID ---------------- */
    suspend fun getById(id: String): Beneficiary? {
        val doc = db.collection(COLLECTION)
            .document(id)
            .get()
            .await()

        return doc.toObject(Beneficiary::class.java)
            ?.copy(id = doc.id)
    }

    /* ---------------- ADD ---------------- */
    suspend fun add(beneficiary: Beneficiary) {
        db.collection(COLLECTION)
            .add(
                beneficiary.copy(id = "")
            )
            .await()
    }

    /* ---------------- UPDATE ---------------- */
    suspend fun update(beneficiary: Beneficiary) {
        if (beneficiary.id.isBlank()) return

        db.collection(COLLECTION)
            .document(beneficiary.id)
            .set(
                beneficiary.copy(id = "")
            )
            .await()
    }

    /* ---------------- REMOVE ---------------- */
    suspend fun remove(id: String) {
        db.collection(COLLECTION)
            .document(id)
            .delete()
            .await()
    }
}
