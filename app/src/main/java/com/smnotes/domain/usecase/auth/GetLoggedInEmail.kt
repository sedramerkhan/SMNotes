package com.smnotes.domain.usecase.auth

import com.smnotes.domain.repository.AuthRepository

class GetLoggedInEmail(private val authRepository: AuthRepository) {
    operator fun invoke(): String? = authRepository.getLoggedInEmail()
}
