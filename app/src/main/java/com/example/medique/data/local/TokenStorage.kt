package com.example.medique.data.local


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "mediqueue_session")

class TokenStorage(private val context: Context) {

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_USER_ROLE = stringPreferencesKey("user_role")
    }

    val accessTokenFlow: Flow<String?> = context.dataStore.data.map { it[KEY_ACCESS_TOKEN] }
    val refreshTokenFlow: Flow<String?> = context.dataStore.data.map { it[KEY_REFRESH_TOKEN] }
    val userRoleFlow: Flow<String?> = context.dataStore.data.map { it[KEY_USER_ROLE] }

    suspend fun saveSession(accessToken: String, refreshToken: String, role: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = accessToken
            prefs[KEY_REFRESH_TOKEN] = refreshToken
            prefs[KEY_USER_ROLE] = role
        }
    }

    suspend fun updateAccessToken(newAccessToken: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = newAccessToken
        }
    }

    suspend fun getAccessToken(): String? = accessTokenFlow.first()
    suspend fun getRefreshToken(): String? = refreshTokenFlow.first()

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}