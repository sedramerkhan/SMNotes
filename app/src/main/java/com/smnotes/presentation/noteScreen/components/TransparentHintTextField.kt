package com.smnotes.presentation.noteScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = { Text(text = hint, style = textStyle) },
        singleLine = singleLine,
        textStyle = textStyle,
        modifier = modifier.fillMaxWidth(),
        colors =  TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        placeholderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
//        cursorColor = MaterialTheme.colors.secondary
    ))
//    Box(
//        modifier = modifier
//    ) {
//
//        BasicTextField(
//            value = text,
//            onValueChange =,
//            singleLine = singleLine,
//            textStyle = textStyle,
//            modifier = Modifier
//                .fillMaxWidth()
//                .onFocusChanged {
//                    onFocusChange(it)
//                }
//        )
//        if (isHintVisible) {
//            Text(text = hint, style = textStyle, color = Color.DarkGray)
//        }
//    }
}