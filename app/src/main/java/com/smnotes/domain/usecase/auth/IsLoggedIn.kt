package com.smnotes.domain.usecase

import com.smnotes.domain.repository.AuthRepository

class IsLoggedIn(private val authRepository: AuthRepository) {
    operator fun invoke(): Boolean = authRepository.isLoggedIn()
}
