package com.smnotes.presentation.authScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    onSuccess: () -> Unit,
    onContinueWithoutAccount: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is AuthScreenEvent.Success -> onSuccess()
            }
        }
    }

    val errorMessage = (viewModel.uiState as? AuthUiState.Error)?.message
    val isLoading = viewModel.uiState is AuthUiState.Loading

    if (viewModel.mode == AuthMode.LOGIN) {
        LoginView(
            email = viewModel.email,
            password = viewModel.password,
            isLoading = isLoading,
            errorMessage = errorMessage,
            onEmailChange = { viewModel.email = it },
            onPasswordChange = { viewModel.password = it },
            onSignIn = { viewModel.submit() },
            onNavigateToRegister = { viewModel.switchMode() },
            onContinueWithoutAccount = onContinueWithoutAccount,
        )
    } else {
        RegisterView(
            email = viewModel.email,
            password = viewModel.password,
            confirmPassword = viewModel.confirmPassword,
            isLoading = isLoading,
            errorMessage = errorMessage,
            onEmailChange = { viewModel.email = it },
            onPasswordChange = { viewModel.password = it },
            onConfirmPasswordChange = { viewModel.confirmPassword = it },
            onCreateAccount = { viewModel.submit() },
            onNavigateToLogin = { viewModel.switchMode() },
        )
    }
}
