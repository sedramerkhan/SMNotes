package com.smnotes.presentation.notesScreen.components.drawer

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.smnotes.R

enum class DrawerItems(@StringRes val labelRes: Int, val value: String, val icon: ImageVector) {
    HOME(R.string.drawer_home, "Home", Icons.Default.Home),
    IMPORTANT(R.string.drawer_important, "Important", Icons.Default.Star)
}

fun getDrawerItem(value: String): DrawerItems? {
    val map =  DrawerItems.entries.associateBy( DrawerItems::value)
    return map[value]
}

fun getDrawerItems() = DrawerItems.entries