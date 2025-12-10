package com.example.lojasocial.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.lojasocial.models.Employee
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : AuthRepository {

    override suspend fun login(email: String, password: String): ResultWrapper<Employee?> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val uid = auth.currentUser?.uid ?: return ResultWrapper.Error(Exception("No UID"))

            val snap = db.collection("employees").document(uid).get().await()
            val user = snap.toObject(Employee::class.java)

            ResultWrapper.Success(user)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    override suspend fun register(employee: Employee, password: String): ResultWrapper<Boolean> {
        return try {
            val result = auth.createUserWithEmailAndPassword(employee.email!!, password).await()
            val uid = result.user?.uid ?: return ResultWrapper.Error(Exception("UID null"))

            db.collection("employees")
                .document(uid)
                .set(employee.copy(id = uid))
                .await()

            ResultWrapper.Success(true)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    override suspend fun recover(email: String): ResultWrapper<Boolean> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            ResultWrapper.Success(true)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }
}
