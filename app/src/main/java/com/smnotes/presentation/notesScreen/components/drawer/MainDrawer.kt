package com.smnotes.presentation.notesScreen.components.drawer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smnotes.R

@Composable
fun MainDrawer(
    selected: DrawerItems,
    onItemSelected: (DrawerItems) -> Unit,
    isDark: Boolean,
    toggleLightTheme: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(top = 60.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopDrawerView()
            DrawerItemsView(isDark = isDark, selected = selected, onItemSelected = onItemSelected)
            Switch(isDark = isDark, toggleLightTheme = toggleLightTheme)
        }
    }
}


@Composable
fun TopDrawerView() {
    Card(modifier = Modifier.clip(CircleShape)) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(id = R.drawable.img), contentDescription = null,

            )
    }

    Divider(
        Modifier
            .fillMaxWidth(.8f)
            .padding(top = 8.dp, bottom = 16.dp)
    )

}

@Composable
fun DrawerItemsView(
    isDark: Boolean,
    selected: DrawerItems,
    onItemSelected: (DrawerItems) -> Unit,
) {
    getDrawerItems().forEach {
        val backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.25f)
        val color2 =
            if (selected == it && !isDark)
                MaterialTheme.colors.primary
            else MaterialTheme.colors.onSurface.copy(alpha = if (isDark) .8f else .6f)
        val roundedCornerShape = RoundedCornerShape(20.dp)
        val width =
            animateDpAsState(
                targetValue = if (selected == it) 28.dp else 2.dp, animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing,
                )
            )
        Box(
            Modifier
                .fillMaxWidth(.9f).border(width.value, backgroundColor, roundedCornerShape)
                .clip(roundedCornerShape).clickable { onItemSelected(it) }
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = it.icon, contentDescription = it.value, tint = color2)
                Text(
                    text = it.value,
                    style = MaterialTheme.typography.h3,
                    color = color2
                )
            }

        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun Switch(isDark: Boolean, toggleLightTheme: () -> Unit) {
    Divider(
        Modifier
            .fillMaxWidth(.8f)
            .padding(top = 8.dp, bottom = 16.dp)
    )
    Row(
        modifier = Modifier.fillMaxWidth(.9f).padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        val on_off = if (isDark) "off" else "on"
        Text(
            text = "Turn $on_off dark mode",
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSurface
        )
        Switch(
            checked = isDark,
            onCheckedChange = { toggleLightTheme() },
            colors = SwitchDefaults.colors(

            )
        )
    }
}