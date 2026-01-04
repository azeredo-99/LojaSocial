package com.example.lojasocial.ui.profile

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileViewModel : ViewModel() {

    var name by mutableStateOf("")
        private set

    var message by mutableStateOf<String?>(null)
        private set

    fun loadProfile() {
        val user = FirebaseAuth.getInstance().currentUser
        name = user?.displayName ?: "Utilizador"
    }

    fun updateName(newName: String) {
        val user = FirebaseAuth.getInstance().currentUser ?: return

        val request = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()

        user.updateProfile(request)
            .addOnSuccessListener {
                name = newName
                message = "Nome atualizado com sucesso"
            }
            .addOnFailureListener {
                message = "Erro ao atualizar nome"
            }
    }

    fun updatePassword(newPassword: String) {
        val user = FirebaseAuth.getInstance().currentUser ?: return

        if (newPassword.length < 6) {
            message = "A palavra-passe deve ter pelo menos 6 caracteres"
            return
        }

        user.updatePassword(newPassword)
            .addOnSuccessListener {
                message = "Palavra-passe atualizada com sucesso"
            }
            .addOnFailureListener {
                message = "Erro ao atualizar palavra-passe"
            }
    }
}
