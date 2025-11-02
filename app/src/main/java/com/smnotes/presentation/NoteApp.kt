package com.smnotes.presentation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.smnotes.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoteApp : Application() {

    var isDark by mutableStateOf(false)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NoteApp)
            modules(appModule)
        }
    }

    fun toggleLightTheme() {
        isDark = !isDark
    }
}