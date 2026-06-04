package com.smnotes.presentation.authScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smnotes.domain.error.AuthError
import com.smnotes.domain.sync.SyncManager
import com.smnotes.domain.usecase.auth.AuthUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authUseCases: AuthUseCases,
    private val syncManager: SyncManager
) : ViewModel() {

    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    var mode by mutableStateOf(AuthMode.LOGIN)
        private set

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    private val _events = MutableSharedFlow<AuthScreenEvent>()
    val events = _events.asSharedFlow()

    fun switchMode() {
        mode = if (mode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
        uiState = AuthUiState.Idle
    }

    fun submit() {
        if (mode == AuthMode.LOGIN) login() else register()
    }

    private fun login() {
        viewModelScope.launch {
            uiState = AuthUiState.Loading
            val result = authUseCases.login(email.trim(), password)
            uiState = if (result.isSuccess) {
                viewModelScope.launch { syncManager.syncOnLogin() }
                _events.emit(AuthScreenEvent.Success)
                AuthUiState.Idle
            } else {
                AuthUiState.Error(result.toErrorMessage())
            }
        }
    }

    private fun register() {
        if (password != confirmPassword) {
            uiState = AuthUiState.Error("Passwords do not match")
            return
        }
        if (!isPasswordValid(password)) {
            uiState = AuthUiState.Error(
                "Password must be at least 9 characters with 1 uppercase letter and 1 digit"
            )
            return
        }
        viewModelScope.launch {
            uiState = AuthUiState.Loading
            val result = authUseCases.register(email.trim(), password)
            uiState = if (result.isSuccess) {
                // Auto-login after register
                authUseCases.login(email.trim(), password)
                viewModelScope.launch { syncManager.syncOnLogin() }
                _events.emit(AuthScreenEvent.Success)
                AuthUiState.Idle
            } else {
                AuthUiState.Error(result.toErrorMessage())
            }
        }
    }

    private fun isPasswordValid(pw: String): Boolean =
        pw.length >= 9 && pw.any { it.isUpperCase() } && pw.any { it.isDigit() }

    private fun Result<*>.toErrorMessage(): String =
        when (val e = exceptionOrNull()) {
            is AuthError -> e.toUserMessage()
            else -> "Unexpected error"
        }
}
