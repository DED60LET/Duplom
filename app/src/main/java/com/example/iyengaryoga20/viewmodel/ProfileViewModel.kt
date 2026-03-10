package com.example.iyengaryoga20.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iyengaryoga20.data.SettingsManager
import com.example.iyengaryoga20.model.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsManager = SettingsManager(application)

    // Состояния настроек (превращаем их в удобные для UI переменные)
    val areNotificationsEnabled = settingsManager.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val areTestNotificationsEnabled = settingsManager.testNotificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val currentTheme = settingsManager.appTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.Light)

    // Методы изменения настроек
    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsManager.setNotificationsEnabled(enabled) }
    }

    fun setTestNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsManager.setTestNotificationsEnabled(enabled) }
    }

    fun setAppTheme(theme: AppTheme) {
        viewModelScope.launch { settingsManager.setAppTheme(theme) }
    }
}