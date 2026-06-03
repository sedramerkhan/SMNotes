package com.smnotes.presentation.authScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smnotes.R
import com.smnotes.presentation.utils.snackbar.NormalSnackbar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AuthScreen(
    onSuccess: () -> Unit,
    onContinueWithoutAccount: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is AuthScreenEvent.Success -> onSuccess()
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(uiState.message)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .systemBarsPadding()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (viewModel.mode == AuthMode.LOGIN)
                        stringResource(R.string.sign_in)
                    else
                        stringResource(R.string.create_account),
                    style = MaterialTheme.typography.h4
                )

                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text(stringResource(R.string.email)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is AuthUiState.Loading
                )

                PasswordField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = stringResource(R.string.password),
                    enabled = uiState !is AuthUiState.Loading
                )

                if (viewModel.mode == AuthMode.REGISTER) {
                    PasswordField(
                        value = viewModel.confirmPassword,
                        onValueChange = { viewModel.confirmPassword = it },
                        label = stringResource(R.string.confirm_password),
                        enabled = uiState !is AuthUiState.Loading
                    )
                    Text(
                        text = stringResource(R.string.password_requirements),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }

                Button(
                    onClick = { viewModel.submit() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is AuthUiState.Loading
                ) {
                    if (uiState is AuthUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colors.onPrimary
                        )
                    } else {
                        Text(
                            if (viewModel.mode == AuthMode.LOGIN)
                                stringResource(R.string.sign_in)
                            else
                                stringResource(R.string.create_account)
                        )
                    }
                }

                TextButton(onClick = { viewModel.switchMode() }) {
                    Text(
                        text = if (viewModel.mode == AuthMode.LOGIN)
                            stringResource(R.string.toggle_to_register)
                        else
                            stringResource(R.string.toggle_to_login),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }

            TextButton(
                onClick = onContinueWithoutAccount,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Text(stringResource(R.string.continue_without_account), color = MaterialTheme.colors.onSurface)
            }

            NormalSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        enabled = enabled,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
