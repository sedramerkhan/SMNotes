package com.smnotes.data.remote.datasource

import com.smnotes.data.remote.dto.auth.AuthResponseDto

interface AuthRemoteDataSource {
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<AuthResponseDto>
    suspend fun refresh(refreshToken: String): Result<AuthResponseDto>
}
