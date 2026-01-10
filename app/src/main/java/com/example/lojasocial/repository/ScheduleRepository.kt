package com.example.lojasocial.repository

import android.annotation.SuppressLint
import com.example.lojasocial.models.Schedule
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object ScheduleRepository {

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private const val COLLECTION = "schedules"

    /* ---------------- GET ALL ---------------- */
    suspend fun getAll(): List<Schedule> {
        return db.collection(COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toObject(Schedule::class.java)
                    ?.copy(id = doc.id)
            }
    }

    /* ---------------- GET BY ID ---------------- */
    suspend fun getById(id: String): Schedule? {
        val doc = db.collection(COLLECTION)
            .document(id)
            .get()
            .await()

        return doc.toObject(Schedule::class.java)
            ?.copy(id = doc.id)
    }

    /* ---------------- ADD ---------------- */
    suspend fun add(schedule: Schedule) {
        db.collection(COLLECTION)
            .add(schedule.copy(id = ""))
            .await()
    }

    /* ---------------- UPDATE ---------------- */
    suspend fun update(schedule: Schedule) {
        if (schedule.id.isBlank()) return

        db.collection(COLLECTION)
            .document(schedule.id)
            .set(schedule.copy(id = ""))
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
