package com.smnotes.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun logout()
    fun isLoggedIn(): Boolean
    fun getLoggedInEmail(): String?
    suspend fun tryRestoreSession(): Boolean
}
