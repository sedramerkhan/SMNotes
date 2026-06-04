package com.smnotes.domain.usecase.auth

import com.smnotes.domain.repository.AuthRepository

class Register(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        authRepository.register(email, password)
}
