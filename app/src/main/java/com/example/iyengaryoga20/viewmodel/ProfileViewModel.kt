package com.example.iyengaryoga20.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iyengaryoga20.data.SettingsManager
import com.example.iyengaryoga20.data.db.AppDatabase
import com.example.iyengaryoga20.data.db.UserEntity
import com.example.iyengaryoga20.model.AppTheme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val settingsManager = SettingsManager(application)

    // --- БЕРЕМ ПОЛЬЗОВАТЕЛЯ ИЗ БАЗЫ ---
    val currentUser: StateFlow<UserEntity?> = settingsManager.currentUserPhone
        .flatMapLatest { phone ->
            if (phone != null) db.userDao().getUserFlow(phone)
            else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val areNotificationsEnabled = settingsManager.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val areTestNotificationsEnabled = settingsManager.testNotificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val currentTheme = settingsManager.appTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.Light)

    fun setNotificationsEnabled(enabled: Boolean) { viewModelScope.launch { settingsManager.setNotificationsEnabled(enabled) } }
    fun setTestNotificationsEnabled(enabled: Boolean) { viewModelScope.launch { settingsManager.setTestNotificationsEnabled(enabled) } }
    fun setAppTheme(theme: AppTheme) { viewModelScope.launch { settingsManager.setAppTheme(theme) } }
    fun updateAvatar(uri: String) {
        viewModelScope.launch {
            val user = currentUser.value
            if (user != null) {
                // Копируем пользователя с новой аватаркой и сохраняем
                val updatedUser = user.copy(avatarUri = uri)
                db.userDao().updateUser(updatedUser)
            }
        }
    }
}