package com.smnotes.presentation.notesScreen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector


enum class DrawerItems(val value: String,val icon: ImageVector){
    HOME("Home", Icons.Default.Home)
    , IMPORTANT("Important", Icons.Default.Star)
}

fun getDrawerItem(value: String): DrawerItems? {
    val map =  DrawerItems.values().associateBy( DrawerItems::value)
    return map[value]
}

fun getDrawerItems() = DrawerItems.values().toList()