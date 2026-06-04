package com.smnotes.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.smnotes.R
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

private const val DEFAULT_MAX_LENGTH = 255

sealed class TextFieldType {
    object Text : TextFieldType()
    object Email : TextFieldType()
    object Password : TextFieldType()
    object Name : TextFieldType()
    object Phone : TextFieldType()
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    type: TextFieldType = TextFieldType.Text,
    label: String? = null,
    enabled: Boolean = true,
    isRequired: Boolean = false,
    // Runs after the type's default validator; isRequired check always runs first
    validator: ((String) -> String?)? = null,
    // Set false to skip the type's built-in validator (e.g. login password only needs min-length)
    useTypeValidator: Boolean = true,
    // Set true to force all error messages to show immediately (e.g. on button click)
    forceValidate: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    maxLength: Int = DEFAULT_MAX_LENGTH,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var touched by remember { mutableStateOf(false) }
    var wasFocused by remember { mutableStateOf(false) }

    LaunchedEffect(forceValidate) {
        if (forceValidate) touched = true
    }

    val effectiveValidator = buildValidator(type, isRequired, validator, useTypeValidator)
    val error: String? = if (touched) effectiveValidator?.invoke(value) else null

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { new -> if (new.length <= maxLength) onValueChange(new) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { state ->
                    if (state.isFocused) wasFocused = true
                    else if (wasFocused) touched = true
                },
            label = label?.let { { Text(it
            ,
                style = MaterialTheme.typography.body2) } },
            enabled = enabled,
            isError = error != null,
            singleLine = true,
            visualTransformation = if (type is TextFieldType.Password && !passwordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = type.toKeyboardType(),
                imeAction = imeAction,
            ),
            keyboardActions = buildKeyboardActions(onImeAction),
            trailingIcon = if (type is TextFieldType.Password) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) stringResource(R.string.cd_hide_password)
                            else stringResource(R.string.cd_show_password),
                        )
                    }
                }
            } else null,
        )
        Box(Modifier.height(14.dp)) {
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.overline,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}

private fun buildValidator(
    type: TextFieldType,
    isRequired: Boolean,
    custom: ((String) -> String?)?,
    useTypeValidator: Boolean,
): ((String) -> String?)? {
    val chain = mutableListOf<(String) -> String?>()
    if (isRequired) chain.add(FieldValidator::required)
    if (useTypeValidator) type.defaultValidator()?.let { chain.add(it) }
    custom?.let { chain.add(it) }
    return if (chain.isEmpty()) null else FieldValidator.compose(*chain.toTypedArray())
}

private fun TextFieldType.defaultValidator(): ((String) -> String?)? = when (this) {
    TextFieldType.Email -> FieldValidator::email
    TextFieldType.Password -> FieldValidator::password
    TextFieldType.Name -> FieldValidator::name
    else -> null
}

private fun TextFieldType.toKeyboardType(): KeyboardType = when (this) {
    TextFieldType.Email -> KeyboardType.Email
    TextFieldType.Password -> KeyboardType.Password
    TextFieldType.Phone -> KeyboardType.Phone
    else -> KeyboardType.Text
}

private fun buildKeyboardActions(onImeAction: (() -> Unit)?): KeyboardActions =
    if (onImeAction == null) KeyboardActions.Default
    else KeyboardActions(
        onDone = { onImeAction() },
        onNext = { onImeAction() },
        onSearch = { onImeAction() },
        onGo = { onImeAction() },
        onSend = { onImeAction() },
    )
