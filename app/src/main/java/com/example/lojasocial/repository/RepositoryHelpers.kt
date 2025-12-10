package com.example.lojasocial.repository

import com.google.firebase.firestore.DocumentSnapshot

inline fun <reified T> DocumentSnapshot.toModel(): T? {
    return this.toObject(T::class.java)
}
