package com.smnotes.domain.event

import kotlinx.coroutines.flow.MutableSharedFlow

object AuthEventBus {
    val events = MutableSharedFlow<AuthEvent>(extraBufferCapacity = 1)
}

sealed class AuthEvent {
    object SessionExpired : AuthEvent()
}
