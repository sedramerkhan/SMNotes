package com.smnotes.domain.usecase

import com.smnotes.domain.repository.AuthRepository

class TryRestoreSession(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Boolean = authRepository.tryRestoreSession()
}
