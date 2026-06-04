package com.smnotes.domain.usecase.auth

import com.smnotes.domain.repository.AuthRepository

class Logout(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.logout()
}
