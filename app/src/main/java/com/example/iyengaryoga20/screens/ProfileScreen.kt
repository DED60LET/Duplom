package com.example.iyengaryoga20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.iyengaryoga20.model.AppTheme
import com.example.iyengaryoga20.ui.theme.*
import com.example.iyengaryoga20.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    navController: NavController? = null,
    onLogout: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Подписка на данные из ViewModel
    val notificationsEnabled by viewModel.areNotificationsEnabled.collectAsState()
    val testNotificationsEnabled by viewModel.areTestNotificationsEnabled.collectAsState()
    val currentTheme by viewModel.currentTheme.collectAsState()

    // Управление диалогами
    var showThemeDialog by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Цвет фона берется из выбранной темы
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // --- 1. ШАПКА ПРОФИЛЯ ---
        Text(
            text = "Профиль",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Аватарка
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Имя
            Column {
                Text(
                    text = "Иван Иванов",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "+7 900 123-45-67",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Редактировать",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp).clickable { }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 2. КАРТОЧКА АБОНЕМЕНТА ---
        Text(
            text = "Мой абонемент",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            // Карточка окрашивается в основной цвет темы (Лайм/Лаванда)
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "STANDARD 8",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "АКТИВЕН",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Прогресс бар
                Text(
                    text = "Осталось занятий: 4 из 8",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { 0.5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.onPrimary,
                    trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Действует до 25.01.2025",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 3. МЕНЮ НАСТРОЕК ---
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface) // Фон меню
        ) {
            // Кнопка: Тема
            ProfileMenuItem(
                icon = Icons.Default.Palette,
                title = "Тема: ${getThemeName(currentTheme)}",
                onClick = { showThemeDialog = true }
            )

            Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp)

            // Кнопка: Уведомления
            ProfileMenuItem(
                icon = Icons.Default.Notifications,
                title = "Настройка уведомлений",
                onClick = { showNotificationDialog = true }
            )

            Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp)

            // Кнопка: История
            ProfileMenuItem(
                icon = Icons.Default.History,
                title = "История посещений",
                onClick = { /* Заглушка */ }
            )

            Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp)

            // Кнопка: Контакты (Ведет на экран ContactScreen)
            ProfileMenuItem(
                icon = Icons.Default.Place,
                title = "Наши центры и контакты",
                onClick = { navController?.navigate("contacts") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка выхода
        Button(
            onClick = onLogout, // <--- 2. ВЫЗЫВАЕМ ФУНКЦИЮ ЗДЕСЬ
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Выйти из аккаунта", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(80.dp))
    }


    // --- ДИАЛОГ: ВЫБОР ТЕМЫ ---
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Выберите тему") },
            text = {
                Column {
                    ThemeRadioButton("Светлая (Лайм)", AppTheme.Light, currentTheme) { viewModel.setAppTheme(it); showThemeDialog = false }
                    ThemeRadioButton("Тёмная (Pro)", AppTheme.Dark, currentTheme) { viewModel.setAppTheme(it); showThemeDialog = false }
                    ThemeRadioButton("Лаванда (Релакс)", AppTheme.Lavender, currentTheme) { viewModel.setAppTheme(it); showThemeDialog = false }
                }
            },
            confirmButton = { TextButton(onClick = { showThemeDialog = false }) { Text("Отмена") } }
        )
    }

    // --- ДИАЛОГ: НАСТРОЙКА УВЕДОМЛЕНИЙ ---
    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationDialog = false },
            title = { Text("Уведомления") },
            text = {
                Column {
                    ProfileSwitchItem(
                        icon = Icons.Default.Notifications,
                        title = "Все уведомления",
                        checked = notificationsEnabled,
                        onCheckedChange = { viewModel.setNotificationsEnabled(it) }
                    )

                    if (notificationsEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        ProfileSwitchItem(
                            icon = Icons.Default.Science,
                            title = "Тест при запуске",
                            checked = testNotificationsEnabled,
                            onCheckedChange = { viewModel.setTestNotificationsEnabled(it) }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationDialog = false }) { Text("Готово") }
            }
        )
    }
}

// --- ВСПОМОГАТЕЛЬНЫЕ КОМПОНЕНТЫ ---

@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ProfileSwitchItem(icon: ImageVector, title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun ThemeRadioButton(text: String, theme: AppTheme, current: AppTheme, onClick: (AppTheme) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(selected = (theme == current), onClick = { onClick(theme) })
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = (theme == current), onClick = null)
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

fun getThemeName(theme: AppTheme): String {
    return when(theme) {
        AppTheme.Light -> "Светлая"
        AppTheme.Dark -> "Тёмная"
        AppTheme.Lavender -> "Лаванда"
    }
}