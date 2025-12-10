package com.example.lojasocial.repository

sealed class ResultWrapper<out T> {
    object Loading : ResultWrapper<Nothing>()
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: Throwable) : ResultWrapper<Nothing>()
}

