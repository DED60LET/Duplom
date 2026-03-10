package com.example.iyengaryoga20.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ContactsScreen(navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // --- ШАПКА ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = MaterialTheme.colorScheme.onBackground)
            }
            Text(
                text = "Контакты",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Уральский центр йоги Айенгара",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- СПИСОК ЦЕНТРОВ ---

        // 1. ВАЙНЕРА
        ContactCenterCard(
            context = context,
            title = "Центр на Вайнера 60",
            address = "г. Екатеринбург, ул. Вайнера 60",
            phone = "+79030825099",
            phoneDisplay = "+7 (903) 082-50-99",
            schedule = listOf("Пн-Пт: 7:00 − 21:30", "Сб: 7:00 − 14:00")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. МАЛЫШЕВА
        ContactCenterCard(
            context = context,
            title = "Центр на Малышева 53/1",
            address = "г. Екатеринбург, ул. Малышева 53/1",
            phone = "+79826614041",
            phoneDisplay = "+7 (982) 661-40-41",
            schedule = listOf("Пн-Пт: 7:00 − 21:30", "Сб-Вс: 9:00 − 14:00")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. КУЙБЫШЕВА
        ContactCenterCard(
            context = context,
            title = "Центр на Куйбышева 55А",
            address = "г. Екатеринбург, ул. Куйбышева 55А",
            phone = "+79327003330",
            phoneDisplay = "+7 (932) 700-33-30",
            schedule = listOf("Пн-Пт: 7:00 − 21:30", "Сб-Вс: 9:00 − 14:00")
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- ПОЧТА ---
        Text("Электронная почта", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().clickable { openLink(context, "mailto:yogaekb2005@gmail.com") }
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Text("yogaekb2005@gmail.com", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- СОЦСЕТИ ---
        Text("Мы в социальных сетях", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(12.dp))

        SocialButton(context, "ВКонтакте", "https://vk.com/yogacenterekb", Icons.Default.Public)
        Spacer(modifier = Modifier.height(8.dp))
        SocialButton(context, "Telegram канал", "https://t.me/Ural_Iyengar_Yoga", Icons.Default.Send)
        Spacer(modifier = Modifier.height(8.dp))
        SocialButton(context, "Уральская Конвенция", "https://t.me/yoga_arena2025", Icons.Default.Groups)

        Spacer(modifier = Modifier.height(40.dp))
    }
}

// Карточка одного филиала
@Composable
fun ContactCenterCard(
    context: Context,
    title: String,
    address: String,
    phone: String,
    phoneDisplay: String,
    schedule: List<String>
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Заголовок
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(12.dp))

            // Адрес (Кликабельный -> Карта)
            Row(modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    // Открываем карту по адресу
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
                    intent.setPackage("com.google.android.apps.maps") // Пытаемся открыть Google Maps
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        // Если нет Google Maps, открываем любую карту или браузер
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
                        context.startActivity(browserIntent)
                    }
                }
                .padding(vertical = 4.dp)
            ) {
                Icon(Icons.Default.Place, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(address, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Телефон
            Row(modifier = Modifier
                .clickable { openLink(context, "tel:$phone") }
                .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Phone, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(phoneDisplay, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            }

            // WhatsApp
            Row(modifier = Modifier
                .clickable { openLink(context, "https://wa.me/$phone") } // Простая ссылка на WA
                .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Chat, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Написать в WhatsApp", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

            // Режим работы
            schedule.forEach { time ->
                Text(time, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun SocialButton(context: Context, text: String, url: String, icon: ImageVector) {
    OutlinedButton(
        onClick = { openLink(context, url) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(icon, null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

// Утилита для открытия ссылок
fun openLink(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}