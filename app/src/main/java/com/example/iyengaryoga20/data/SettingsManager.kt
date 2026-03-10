package com.example.iyengaryoga20.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.iyengaryoga20.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    companion object {
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val TEST_NOTIFICATIONS_ENABLED = booleanPreferencesKey("test_notifications_enabled")
        val APP_THEME = stringPreferencesKey("app_theme")
        val CURRENT_USER_PHONE = stringPreferencesKey("current_user_phone")
    }

    val notificationsEnabled = context.dataStore.data.map { it[NOTIFICATIONS_ENABLED] ?: true }
    val testNotificationsEnabled = context.dataStore.data.map { it[TEST_NOTIFICATIONS_ENABLED] ?: true }

    val appTheme: Flow<AppTheme> = context.dataStore.data.map { preferences ->
        try { AppTheme.valueOf(preferences[APP_THEME] ?: AppTheme.Light.name) }
        catch (e: Exception) { AppTheme.Light }
    }

    // --- АВТОРИЗАЦИЯ ---
    val currentUserPhone: Flow<String?> = context.dataStore.data.map { it[CURRENT_USER_PHONE] }

    suspend fun setNotificationsEnabled(enabled: Boolean) { context.dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled } }
    suspend fun setTestNotificationsEnabled(enabled: Boolean) { context.dataStore.edit { it[TEST_NOTIFICATIONS_ENABLED] = enabled } }
    suspend fun setAppTheme(theme: AppTheme) { context.dataStore.edit { it[APP_THEME] = theme.name } }

    suspend fun loginUser(phone: String) { context.dataStore.edit { it[CURRENT_USER_PHONE] = phone } }
    suspend fun logoutUser() { context.dataStore.edit { it.remove(CURRENT_USER_PHONE) } }
}