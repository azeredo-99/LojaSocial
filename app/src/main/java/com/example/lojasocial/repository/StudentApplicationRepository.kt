package com.example.lojasocial.repository

import android.annotation.SuppressLint
import com.example.lojasocial.models.StudentApplication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object StudentApplicationRepository {

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    private const val COLLECTION = "student_applications"

    suspend fun submit(app: StudentApplication): ResultWrapper<String> {
        return try {
            val docRef = db.collection(COLLECTION)
                .add(
                    app.copy(
                        id = "",
                        estado = "PENDENTE",
                        createdAt = Timestamp.now()
                    )
                )
                .await()

            ResultWrapper.Success(docRef.id)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }
}
