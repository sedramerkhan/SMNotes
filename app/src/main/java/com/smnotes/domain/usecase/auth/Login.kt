package com.smnotes.domain.usecase

import com.smnotes.domain.repository.AuthRepository

class Login(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        authRepository.login(email, password)
}
