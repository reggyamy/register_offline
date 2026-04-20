package com.reggya.registeroffline.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.reggya.registeroffline.domain.model.Profile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_prefs"
)

@Singleton
class UserDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val gson = Gson()


    companion object {
        val KEY_USER_JSON = stringPreferencesKey("user_json")
    }

    suspend fun saveUser(profile: Profile) {
        val json = gson.toJson(profile)
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_JSON] = json
        }
    }

    fun getUser(): Flow<Profile?> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_USER_JSON]?.let { json ->
                gson.fromJson(json, Profile::class.java)
            }
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}