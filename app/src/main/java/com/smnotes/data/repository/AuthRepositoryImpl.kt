package com.smnotes.data.repository

import com.smnotes.data.auth.SessionStore
import com.smnotes.data.database.NoteDao
import com.smnotes.data.remote.datasource.AuthRemoteDataSource
import com.smnotes.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val sessionStore: SessionStore,
    private val noteDao: NoteDao
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        val result = remoteDataSource.login(email, password)
        return result.fold(
            onSuccess = { dto ->
                sessionStore.saveTokens(dto.accessToken, dto.refreshToken)
                sessionStore.saveEmail(email)
                Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )
    }

    override suspend fun register(email: String, password: String): Result<Unit> {
        return remoteDataSource.register(email, password)
    }

    override suspend fun logout() {
        noteDao.resetAllSyncStatus()
        sessionStore.clearAll()
    }

    override fun isLoggedIn(): Boolean = sessionStore.isLoggedIn()

    override fun getLoggedInEmail(): String? = sessionStore.getEmail()

    override suspend fun tryRestoreSession(): Boolean {
        val refreshToken = sessionStore.getRefreshToken() ?: return false
        return remoteDataSource.refresh(refreshToken).fold(
            onSuccess = { dto ->
                sessionStore.saveTokens(dto.accessToken, dto.refreshToken)
                true
            },
            onFailure = {
                sessionStore.clearAll()
                false
            }
        )
    }
}
