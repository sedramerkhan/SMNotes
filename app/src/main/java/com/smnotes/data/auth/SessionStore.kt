package com.smnotes.data.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

class SessionStore(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "smnotes_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var accessToken: String? = null

    fun saveTokens(access: String, refresh: String) {
        accessToken = access
        saveRefreshToken(refresh)
    }

    fun saveRefreshToken(token: String) = prefs.edit { putString(KEY_REFRESH_TOKEN, token) }
    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun saveEmail(email: String) = prefs.edit { putString(KEY_EMAIL, email) }
    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

    fun isLoggedIn(): Boolean = getRefreshToken() != null

    fun clearAll() {
        prefs.edit { clear() }
        accessToken = null
    }

    companion object {
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_EMAIL = "email"
    }
}
