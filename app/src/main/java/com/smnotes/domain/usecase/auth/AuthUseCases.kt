package com.smnotes.domain.usecase.auth

data class AuthUseCases(
    val login: Login,
    val register: Register,
    val logout: Logout,
    val isLoggedIn: IsLoggedIn,
    val getLoggedInEmail: GetLoggedInEmail,
    val tryRestoreSession: TryRestoreSession
)
