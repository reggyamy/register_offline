package com.reggya.registeroffline.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val AUTH_PREF = "auth_prefs"
        private const val KEY_TOKEN = "auth_token"
    }

    private fun getPrefs(): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            AUTH_PREF,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToken(token: String) {
        getPrefs().edit { putString(KEY_TOKEN, token) }
    }

    fun getToken(): String? {
        return getPrefs().getString(KEY_TOKEN, null)
    }

    fun clear() {
        getPrefs().edit { clear() }
    }
}