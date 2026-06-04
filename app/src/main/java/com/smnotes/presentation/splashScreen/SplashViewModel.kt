package com.smnotes.presentation.splashScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smnotes.domain.sync.SyncManager
import com.smnotes.domain.usecase.auth.AuthUseCases
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authUseCases: AuthUseCases,
    private val syncManager: SyncManager
) : ViewModel() {

    var isReady by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            // Both calls are fire-and-forget — local notes are always available offline,
            // so there's no reason to block navigation on network operations.
            launch { runCatching { authUseCases.tryRestoreSession() } }
            if (authUseCases.isLoggedIn()) {
                launch { syncManager.syncPending() }
            }
            delay(1200)
            isReady = true
        }
    }
}
