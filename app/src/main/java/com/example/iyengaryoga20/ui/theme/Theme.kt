package com.example.iyengaryoga20.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.iyengaryoga20.model.AppTheme

// 1. Светлая палитра (Стандартная)
private val LightColors = lightColorScheme(
    primary = LimePrimary,
    onPrimary = TextBlack,
    secondary = LimeDark,
    background = BackgroundGrey,
    surface = PureWhite,
    onSurface = TextBlack,
    onBackground = TextBlack,
    error = ErrorRed
)

// 2. Темная палитра
private val DarkColors = darkColorScheme(
    primary = LimePrimary,         // Лайм хорошо светится на темном
    onPrimary = DarkBackground,
    secondary = LimeDark,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,   // Белый текст
    onBackground = DarkTextPrimary,
    error = ErrorRed
)

// 3. Сиреневая палитра
private val LavenderColors = lightColorScheme(
    primary = LavenderPrimary,
    onPrimary = Color.Black,
    secondary = LavenderDark,
    background = LavenderBackground,
    surface = PureWhite,
    onSurface = Color.Black,
    onBackground = Color.Black,
    tertiary = LavenderSurface
)

@Composable
fun IyengarYoga20Theme(
    appTheme: AppTheme = AppTheme.Light, // Принимаем выбранную тему
    content: @Composable () -> Unit
) {
    // Выбираем палитру
    val colorScheme = when (appTheme) {
        AppTheme.Light -> LightColors
        AppTheme.Dark -> DarkColors
        AppTheme.Lavender -> LavenderColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Красим статус бар в цвет фона или праймари
            window.statusBarColor = colorScheme.background.toArgb()

            // Если тема темная, иконки белые. Если светлая - черные.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = (appTheme != AppTheme.Dark)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}