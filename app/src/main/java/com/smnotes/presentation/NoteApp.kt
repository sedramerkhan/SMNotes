package com.smnotes.presentation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.setValue
import com.smnotes.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoteApp : Application() {

    private val _isDark = mutableStateOf(false)
    val isDark: State<Boolean> = _isDark
    
    var isDarkValue: Boolean
        get() = _isDark.value
        set(value) { _isDark.value = value }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NoteApp)
            modules(appModule)
        }
    }

    fun toggleLightTheme() {
        _isDark.value = !_isDark.value
    }
}