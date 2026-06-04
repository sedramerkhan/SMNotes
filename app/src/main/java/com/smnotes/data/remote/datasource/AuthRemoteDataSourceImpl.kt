package com.smnotes.data.remote.datasource

import com.smnotes.data.remote.dto.auth.AuthRequestDto
import com.smnotes.data.remote.dto.auth.AuthResponseDto
import com.smnotes.data.remote.dto.auth.RefreshRequestDto
import com.smnotes.domain.error.AuthError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthRemoteDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String
) : AuthRemoteDataSource {

    override suspend fun register(email: String, password: String): Result<Unit> {
        val response = runCatching {
            client.post("$baseUrl/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(AuthRequestDto(email, password))
            }
        }.getOrElse { return Result.failure(AuthError.NetworkUnavailable) }
        return when (response.status) {
            HttpStatusCode.OK         -> Result.success(Unit)
            HttpStatusCode.Conflict   -> Result.failure(AuthError.EmailAlreadyExists)
            HttpStatusCode.BadRequest -> Result.failure(AuthError.InvalidInput)
            else                      -> Result.failure(AuthError.Unknown(response.status.value))
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthResponseDto> {
        val response = runCatching {
            client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(AuthRequestDto(email, password))
            }
        }.getOrElse { return Result.failure(AuthError.NetworkUnavailable) }
        return when (response.status) {
            HttpStatusCode.OK           -> Result.success(response.body())
            HttpStatusCode.Unauthorized -> Result.failure(AuthError.InvalidCredentials)
            HttpStatusCode.BadRequest   -> Result.failure(AuthError.InvalidInput)
            else                        -> Result.failure(AuthError.Unknown(response.status.value))
        }
    }

    override suspend fun refresh(refreshToken: String): Result<AuthResponseDto> {
        val response = runCatching {
            client.post("$baseUrl/auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshRequestDto(refreshToken))
            }
        }.getOrElse { return Result.failure(AuthError.NetworkUnavailable) }
        return when (response.status) {
            HttpStatusCode.OK -> Result.success(response.body())
            else              -> Result.failure(AuthError.Unknown(response.status.value))
        }
    }
}
