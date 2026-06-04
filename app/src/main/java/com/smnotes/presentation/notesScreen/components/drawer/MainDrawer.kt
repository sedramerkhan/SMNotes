package com.smnotes.presentation.notesScreen.components.drawer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smnotes.R

@Composable
fun MainDrawer(
    selected: DrawerItems,
    onItemSelected: (DrawerItems) -> Unit,
    isDark: Boolean,
    toggleLightTheme: () -> Unit,
    isLoggedIn: Boolean = false,
    userEmail: String? = null,
    onSignIn: () -> Unit = {},
    onSignOut: () -> Unit = {}
) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(top = 60.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopDrawerView()
            DrawerItemsView(isDark = isDark, selected = selected, onItemSelected = onItemSelected)
            Divider(Modifier.fillMaxWidth(.8f).padding(top = 8.dp, bottom = 16.dp))
            AuthSection(
                isLoggedIn = isLoggedIn,
                userEmail = userEmail,
                onSignIn = onSignIn,
                onSignOut = onSignOut
            )
            Divider(Modifier.fillMaxWidth(.8f).padding(top = 8.dp, bottom = 16.dp))

            Switch(isDark = isDark, toggleLightTheme = toggleLightTheme)
        }
    }
}

@Composable
fun AuthSection(
    isLoggedIn: Boolean,
    userEmail: String?,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit
) {
    if (isLoggedIn && userEmail != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(bottom = 9.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            TextButton(onClick = onSignOut) {
                Text(stringResource(R.string.sign_out), color = MaterialTheme.colors.onSurface)
            }
        }
    } else {
        TextButton(
            onClick = onSignIn,
            modifier = Modifier.fillMaxWidth(.9f),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = null,
                    modifier = Modifier.size(24.dp),)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.sign_in), color = MaterialTheme.colors.onSurface)
            }
        }
    }
}

@Composable
fun TopDrawerView() {
    Card(modifier = Modifier.clip(CircleShape)) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(id = R.drawable.img),
            contentDescription = null
        )
    }
    Divider(Modifier.fillMaxWidth(.8f).padding(top = 8.dp, bottom = 16.dp))
}

@Composable
fun DrawerItemsView(
    isDark: Boolean,
    selected: DrawerItems,
    onItemSelected: (DrawerItems) -> Unit
) {
    getDrawerItems().forEach {
        val backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.25f)
        val color2 = if (selected == it && !isDark)
            MaterialTheme.colors.primary
        else
            MaterialTheme.colors.onSurface.copy(alpha = if (isDark) .8f else .7f)
        val roundedCornerShape = RoundedCornerShape(20.dp)
        val width = animateDpAsState(
            targetValue = if (selected == it) 28.dp else 2.dp,
            animationSpec = tween(durationMillis = 200, easing = LinearEasing)
        )
        Box(
            Modifier
                .fillMaxWidth(.9f)
                .border(width.value, backgroundColor, roundedCornerShape)
                .clip(roundedCornerShape)
                .clickable { onItemSelected(it) }
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(imageVector = it.icon, contentDescription = stringResource(it.labelRes), tint = color2)
                Text(text = stringResource(it.labelRes), style = MaterialTheme.typography.h3, color = color2)
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun Switch(isDark: Boolean, toggleLightTheme: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(if (isDark) R.string.turn_off_dark_mode else R.string.turn_on_dark_mode),
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSurface
        )
        Switch(
            checked = isDark,
            onCheckedChange = { toggleLightTheme() },
            colors = SwitchDefaults.colors()
        )
    }
}
