package com.example.lojasocial.repository

import android.annotation.SuppressLint
import com.example.lojasocial.models.StudentApplication
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object StudentApplicationsAdminRepository {

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private const val COLLECTION = "student_applications"

    suspend fun getPending(): ResultWrapper<List<StudentApplication>> {
        return try {
            val snap = db.collection(COLLECTION)
                .whereEqualTo("estado", "PENDENTE")
                .get()
                .await()

            val list = snap.documents.mapNotNull { doc ->
                doc.toObject(StudentApplication::class.java)?.copy(id = doc.id)
            }

            ResultWrapper.Success(list)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    suspend fun markAccepted(appId: String): ResultWrapper<Unit> {
        return try {
            db.collection(COLLECTION)
                .document(appId)
                .update("estado", "ACEITE")
                .await()
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    suspend fun markRejected(appId: String): ResultWrapper<Unit> {
        return try {
            db.collection(COLLECTION)
                .document(appId)
                .update("estado", "REJEITADA")
                .await()
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }
}
