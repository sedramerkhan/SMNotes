package com.smnotes.presentation.splashScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smnotes.domain.repository.AuthRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var isReady by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            // Navigation fires only after complete.
            val restore = async { runCatching { authRepository.tryRestoreSession() } }
            restore.await()
            isReady = true
        }
    }
}
