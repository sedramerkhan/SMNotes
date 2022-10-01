package com.smnotes.presentation.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val STATUS_BAR_HEIGHT = 15.dp

@Composable
fun Modifier.defaultModifier(horizontal: Dp, vertical: Dp) = fillMaxSize().padding(
    top = STATUS_BAR_HEIGHT + vertical,
    bottom = vertical,
    start = horizontal,
    end = horizontal
)