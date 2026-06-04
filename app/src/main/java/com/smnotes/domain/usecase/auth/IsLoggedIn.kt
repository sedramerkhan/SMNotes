package com.smnotes.domain.usecase.auth

import com.smnotes.domain.repository.AuthRepository

class IsLoggedIn(private val authRepository: AuthRepository) {
    operator fun invoke(): Boolean = authRepository.isLoggedIn()
}
