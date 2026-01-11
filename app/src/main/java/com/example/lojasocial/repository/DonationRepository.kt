package com.example.lojasocial.repository

import android.annotation.SuppressLint
import com.example.lojasocial.models.Donation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object DonationRepository {

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private const val COLLECTION = "donations"

    /* ---------- GET ALL ---------- */
    suspend fun getAll(): List<Donation> {
        return db.collection(COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(Donation::class.java)?.copy(id = doc.id)
            }
    }

    /* ---------- GET BY ID ---------- */
    suspend fun getById(id: String): Donation? {
        val doc = db.collection(COLLECTION)
            .document(id)
            .get()
            .await()

        return doc.toObject(Donation::class.java)?.copy(id = doc.id)
    }

    /* ---------- ADD ---------- */
    suspend fun add(donation: Donation) {
        db.collection(COLLECTION)
            .add(donation.copy(id = ""))
            .await()
    }

    /* ---------- UPDATE ---------- */
    suspend fun update(donation: Donation) {
        if (donation.id.isBlank()) return

        db.collection(COLLECTION)
            .document(donation.id)
            .set(donation.copy(id = ""))
            .await()
    }

    /* ---------- UPDATE RECEIVED ---------- */
    suspend fun updateReceived(id: String, received: Boolean) {
        db.collection(COLLECTION)
            .document(id)
            .update("received", received)
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
