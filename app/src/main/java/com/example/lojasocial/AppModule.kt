package com.example.lojasocial

import com.example.lojasocial.repository.AuthRepository
import com.example.lojasocial.repository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        db: FirebaseFirestore
    ): AuthRepository {
        return AuthRepositoryImpl(auth, db)
    }
}
