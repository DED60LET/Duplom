package com.example.iyengaryoga20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class TeacherData(val name: String, val certificate: String)

@Composable
fun TeachersScreen(navController: NavController?) {
    // Список преподавателей
    val teachersList = listOf(
        TeacherData("Юлия Бахарева", "Сертификат RIMYI Junior Int. III (2015)"),
        TeacherData("Ольга Бухнер", "Сертификат RIMYI Junior Int.III (2016)"),
        TeacherData("Евгения Шипулина", "Сертификат RIMYI Junior Int.II (2019)"),
        TeacherData("Константин Перетятькин", "Сертификат RIMYI Junior Int. I (2007)"),
        TeacherData("Виктория Десятова", "Сертификат RIMYI Junior Int. I (2020)"),
        TeacherData("Анастасия Иванова", "Сертификат RIMYI Junior Int. I (2020)"),
        TeacherData("Олеся Яговкина", "Сертификат RIMYI Junior Int.I (2019)"),
        TeacherData("Наталья Ваганова", "Сертификат RIMYI Junior Int. I (2013)"),
        TeacherData("Елена Кочкина", "Сертификат Introductory II"),
        TeacherData("Дмитрий Бессонов", "Сертификат RIMYI Introductory II (2012)"),
        TeacherData("Ирина Нагурная", "Сертификат RIMYI Introductory II (2009)"),
        TeacherData("Анастасия Михалькова", "Сертификат RIMYI Introductory II (2010)"),
        TeacherData("Светлана Павлецова", "Сертификат RIMYI Introductory II (2017)"),
        TeacherData("Елена Чиркова", "Сертификат RIMYI Introductory II (2007)"),
        TeacherData("Алексей Евменов", "Сертификат RIMYI Introductory II (2014)"),
        TeacherData("Елена Ватолина", "Сертификат RIMYI Introductory II (2021)"),
        TeacherData("Вера Шпак", "Сертификат RIMYI Introductory II (2021)"),
        TeacherData("Юлия Коршунова", "Сертификат RIMYI Introductory II (2020)"),
        TeacherData("Вероника Федотова", "Сертификат RIMYI Introductory II (2021)"),
        TeacherData("Мария Тарханова", "Сертификат RIMYI Introductory II (2021)"),
        TeacherData("Мария Неткачева", "Сертификат Level I"),
        TeacherData("Валерия Шумкова", "Сертификат Level I"),
        TeacherData("Эльмира Камалдинова", "Преподаватель центра"),
        TeacherData("Ольга Шакирова", "Преподаватель центра"),
        TeacherData("Анна Зарипова", "Преподаватель центра"),
        TeacherData("Елена Леонова", "Преподаватель центра")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // --- ШАПКА ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = MaterialTheme.colorScheme.onBackground)
            }
            Text(
                text = "Преподаватели",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Подзаголовок
        Text(
            text = "У каждого учителя Центра большой опыт личной практики и преподавания. Наши учителя в реестре сертифицированных преподавателей йоги.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 24.dp)
        )

        // --- СПИСОК ПРЕПОДАВАТЕЛЕЙ ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(teachersList) { teacher ->
                TeacherCard(teacher)
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun TeacherCard(teacher: TeacherData) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Иконка-аватарка
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Имя и сертификат
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = teacher.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = teacher.certificate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}