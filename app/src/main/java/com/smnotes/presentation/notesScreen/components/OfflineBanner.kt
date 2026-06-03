package com.smnotes.presentation.notesScreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smnotes.R

@Composable
fun OfflineBanner(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Text(
            text = stringResource(R.string.offline_banner),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE65100))
                .padding(horizontal = 16.dp, vertical = 6.dp),
            color = Color.White,
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center
        )
    }
}
