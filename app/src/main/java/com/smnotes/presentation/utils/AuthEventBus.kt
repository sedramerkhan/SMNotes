package com.smnotes.presentation.utils

import kotlinx.coroutines.flow.MutableSharedFlow

object AuthEventBus {
    val events = MutableSharedFlow<AuthEvent>(extraBufferCapacity = 1)
}

sealed class AuthEvent {
    object SessionExpired : AuthEvent()
}
