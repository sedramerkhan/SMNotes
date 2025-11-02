package com.smnotes.presentation.notesScreen.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.h2,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = MaterialTheme.colors.onBackground,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)= {},
) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {
                Text(text = title, style = style , modifier = modifier)
        },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        actions = actions,
        navigationIcon = navigationIcon,
        elevation = 0.dp
    )
}