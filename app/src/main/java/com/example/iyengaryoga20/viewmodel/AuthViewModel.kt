package com.example.iyengaryoga20.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iyengaryoga20.data.SettingsManager
import com.example.iyengaryoga20.data.db.AppDatabase
import com.example.iyengaryoga20.data.db.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val settings = SettingsManager(application)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            settings.currentUserPhone.collect { phone ->
                if (phone.isNullOrEmpty()) {
                    _authState.value = AuthState.LoggedOut
                } else {
                    val user = db.userDao().getUser(phone)
                    if (user != null) {
                        _authState.value = AuthState.LoggedIn(user)
                    } else {
                        _authState.value = AuthState.LoggedOut
                    }
                }
            }
        }
    }

    fun onPhoneEntered(phone: String) {
        viewModelScope.launch {
            val user = db.userDao().getUser(phone)
            if (user != null) {
                settings.loginUser(phone)
                _authState.value = AuthState.LoggedIn(user) // Мгновенный переход
            } else {
                _authState.value = AuthState.NeedsRegistration(phone)
            }
        }
    }

    // Убрали параметр card
    fun onRegistrationComplete(phone: String, name: String, email: String, dob: String) {
        viewModelScope.launch {
            // В базу передаем пустое значение для поля cardId
            val newUser = UserEntity(
                phoneNumber = phone,
                fullName = name,
                email = email,
                birthDate = dob,
                cardId = ""
            )

            // 1. Сохраняем в БД
            db.userDao().insertUser(newUser)

            // 2. Сохраняем сессию
            settings.loginUser(phone)

            // 3. МГНОВЕННЫЙ ПЕРЕХОД В ПРИЛОЖЕНИЕ (Без перезапуска)
            _authState.value = AuthState.LoggedIn(newUser)
        }
    }

    fun logout() {
        viewModelScope.launch {
            settings.logoutUser()
            _authState.value = AuthState.LoggedOut
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object LoggedOut : AuthState()
    data class NeedsRegistration(val phone: String) : AuthState()
    data class LoggedIn(val user: UserEntity) : AuthState()
}