package com.smnotes.domain.error

sealed class AuthError : Exception() {
    data object EmailAlreadyExists : AuthError()
    data object InvalidCredentials : AuthError()
    data object InvalidInput : AuthError()
    data class Unknown(val code: Int) : AuthError()

    fun toUserMessage(): String = when (this) {
        is EmailAlreadyExists -> "Email already registered"
        is InvalidCredentials -> "Invalid email or password"
        is InvalidInput       -> "Invalid input"
        is Unknown            -> "Something went wrong (code $code)"
    }
}
