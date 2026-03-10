package com.example.iyengaryoga20

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.iyengaryoga20.data.SettingsManager
import com.example.iyengaryoga20.data.db.UserEntity

import com.example.iyengaryoga20.model.AppTheme
import com.example.iyengaryoga20.screens.LoginScreen
import com.example.iyengaryoga20.screens.RegistrationScreen
import com.example.iyengaryoga20.screens.ScheduleScreen
import com.example.iyengaryoga20.ui.screens.*
import com.example.iyengaryoga20.ui.theme.IyengarYoga20Theme
import com.example.iyengaryoga20.utils.NotificationHelper
import com.example.iyengaryoga20.viewmodel.AuthState
import com.example.iyengaryoga20.viewmodel.AuthViewModel
import com.example.iyengaryoga20.viewmodel.ScheduleViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем канал уведомлений
        NotificationHelper.createNotificationChannel(this)

        setContent {
            val context = LocalContext.current
            val settingsManager = remember { SettingsManager(context) }
            // Подписка на тему
            val currentTheme by settingsManager.appTheme.collectAsState(initial = AppTheme.Light)

            IyengarYoga20Theme(appTheme = currentTheme) {

                // 1. Логика уведомлений (вот функция, которая потерялась)
                NotificationHandler()

                // 2. Логика авторизации
                val authViewModel: AuthViewModel = viewModel()
                val authState by authViewModel.authState.collectAsState()

                when (val state = authState) {
                    is AuthState.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is AuthState.LoggedOut -> {
                        LoginScreen(onLoginClick = { authViewModel.onPhoneEntered(it) })
                    }
                    is AuthState.NeedsRegistration -> {
                        RegistrationScreen(
                            phone = state.phone,
                            onRegisterClick = { name, email, dob, card ->
                                authViewModel.onRegistrationComplete(state.phone, name, email, dob, card)
                            }
                        )
                    }
                    is AuthState.LoggedIn -> {
                        // Если вошли - показываем приложение
                        MainApp(currentUser = state.user, onLogout = { authViewModel.logout() })
                    }
                }
            }
        }
    }
}


@Composable
fun NotificationHandler() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                scope.launch { tryShowNotification(context) }
            }
        }
    )
//Делаю диплом
    LaunchedEffect(Unit) {
        val settingsManager = SettingsManager(context)
        val notificationsEnabled = settingsManager.notificationsEnabled.first()

        if (notificationsEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPermission) {
                    tryShowNotification(context)
                } else {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                tryShowNotification(context)
            }
        }
    }
}

suspend fun tryShowNotification(context: Context) {
    val settingsManager = SettingsManager(context)
    val testEnabled = settingsManager.testNotificationsEnabled.first()

    if (testEnabled) {
        NotificationHelper.showRandomNotification(context)
    }
}
// --------------------------------------


@Composable
fun MainApp(onLogout: () -> Unit, currentUser: UserEntity) { // Сюда функция уже приходит из AuthViewModel
    val navController = rememberNavController()
    val sharedViewModel: ScheduleViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val items = listOf(
                    Triple("news", "Новости", Icons.Default.Article),
                    Triple("schedule", "Расписание", Icons.Default.CalendarToday),
                    Triple("bookings", "Записи", Icons.Default.ConfirmationNumber),
                    Triple("profile", "Профиль", Icons.Default.Person)
                )

                items.forEach { (route, label, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = currentRoute == route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        onClick = {
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "news",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("news") { NewsScreen() }
            composable("schedule") { ScheduleScreen(viewModel = sharedViewModel) }
            composable("bookings") { BookingsScreen(viewModel = sharedViewModel) }
            composable("profile") {
                com.example.iyengaryoga20.ui.screens.ProfileScreen(
                    navController = navController,
                    onLogout = onLogout // <--- ПЕРЕДАЕМ ФУНКЦИЮ В ЭКРАН
                )
            }

            composable("contacts") { ContactsScreen(navController) }
        }
    }
}
