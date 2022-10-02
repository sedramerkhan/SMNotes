package com.smnotes.presentation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteApp : Application(){

    var isDark by mutableStateOf(false)

    fun toggleLightTheme(){
        isDark= !isDark
    }
}