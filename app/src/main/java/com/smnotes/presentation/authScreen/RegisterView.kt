package com.smnotes.presentation.authScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import com.smnotes.presentation.components.CustomButton
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smnotes.R
import com.smnotes.presentation.components.AppTextField
import com.smnotes.presentation.components.FieldValidator
import com.smnotes.presentation.components.TextFieldType

@Composable
fun RegisterView(
    email: String,
    password: String,
    confirmPassword: String,
    isLoading: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onCreateAccount: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val isFormValid =
        email.isNotBlank() &&
        FieldValidator.email(email) == null &&
        FieldValidator.password(password) == null &&
        confirmPassword.isNotBlank() &&
        password == confirmPassword

    val passwordsMismatchMsg = stringResource(R.string.error_passwords_no_match)
    var forceValidate by remember { mutableStateOf(false) }

    val submit = {
        forceValidate = true
        if (isFormValid && !isLoading) onCreateAccount()
    }

    val textColor = Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(64.dp))

            Text(
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = stringResource(R.string.register_subtitle, stringResource(R.string.app_name)),
                style = MaterialTheme.typography.body2,
                color = textColor.copy(alpha = 0.55f),
            )

            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
                    AppTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        type = TextFieldType.Email,
                        label = stringResource(R.string.email),
                        isRequired = true,
                        enabled = !isLoading,
                        forceValidate = forceValidate,
                    )

                    Spacer(Modifier.height(8.dp))
                    AppTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        type = TextFieldType.Password,
                        label = stringResource(R.string.password),
                        isRequired = true,
                        enabled = !isLoading,
                        forceValidate = forceValidate,
                    )
                    Spacer(Modifier.height(8.dp))

                    AppTextField(
                        value = confirmPassword,
                        onValueChange = onConfirmPasswordChange,
                        type = TextFieldType.Password,
                        label = stringResource(R.string.confirm_password),
                        isRequired = true,
                        enabled = !isLoading,
                        forceValidate = forceValidate,
                        imeAction = ImeAction.Done,
                        onImeAction = { submit() },
                        validator = { v ->
                            if (v.isNotBlank() && v != password) passwordsMismatchMsg else null
                        },
                    )

                    Text(
                        text = stringResource(R.string.password_requirements),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
                    )

                    AnimatedVisibility(visible = errorMessage != null) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                        )
                    }
                    Spacer(Modifier.height(24.dp))

                    CustomButton(
                        text = stringResource(R.string.create_account),
                        onClick = { submit() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        isLoading = isLoading,
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.toggle_to_login),
                    textAlign = TextAlign.Center,
                    color = textColor.copy(alpha = 0.8f),
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
