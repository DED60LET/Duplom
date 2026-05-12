package com.example.iyengaryoga20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PrivacyPolicyScreen(navController: NavController?) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- ШАПКА ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = MaterialTheme.colorScheme.onBackground)
            }
            Text(
                text = "Политика конфиденциальности",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // --- ТЕКСТ ПОЛИТИКИ ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "1. Общие положения",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Настоящая политика конфиденциальности определяет порядок обработки персональных данных и меры по обеспечению безопасности персональных данных в мобильном приложении «Уральский центр йоги Айенгара» (далее — Приложение).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                lineHeight = 20.sp
            )

            Text(
                text = "2. Какие данные мы собираем",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "При регистрации в Приложении пользователь предоставляет следующие данные:\n• Номер мобильного телефона\n• Фамилия, Имя, Отчество (ФИО)\n• Адрес электронной почты (e-mail)\n• Дата рождения (опционально)\n\nДанная информация используется исключительно для идентификации клиента в системе студии и учета его бронирований.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                lineHeight = 20.sp
            )

            Text(
                text = "3. Хранение и безопасность",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Ваши персональные данные, а также история записей на занятия, хранятся локально на вашем мобильном устройстве с использованием защищенных механизмов операционной системы Android (Room Database, DataStore).\nМы применяем необходимые организационные и технические меры для защиты данных от неправомерного доступа.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                lineHeight = 20.sp
            )

            Text(
                text = "4. Передача данных третьим лицам",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Приложение может взаимодействовать с открытыми API сервиса Mobifitness для получения актуального расписания. Персональные данные пользователя при этом не передаются третьим лицам без его явного согласия, за исключением случаев, прямо предусмотренных законодательством РФ.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                lineHeight = 20.sp
            )

            Text(
                text = "5. Удаление данных",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Пользователь имеет право в любой момент выйти из своего аккаунта через меню «Профиль», после чего локальная сессия будет завершена. Для полного удаления данных достаточно удалить приложение с устройства.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                lineHeight = 20.sp
            )
        }
    }
}