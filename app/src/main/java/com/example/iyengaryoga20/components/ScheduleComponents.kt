package com.example.iyengaryoga2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.iyengaryoga20.model.YogaClass
import com.example.iyengaryoga20.ui.theme.BackgroundGrey
import com.example.iyengaryoga20.ui.theme.LimeDark
import com.example.iyengaryoga20.ui.theme.LimePrimary
import com.example.iyengaryoga20.ui.theme.LimeSurface
import com.example.iyengaryoga20.ui.theme.PureWhite
import com.example.iyengaryoga20.ui.theme.TextBlack
import com.example.iyengaryoga20.ui.theme.TextGrey
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// 1. Горизонтальный календарь
@Composable
fun CalendarStrip(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val dates = (0..13).map { LocalDate.now().plusDays(it.toLong()) } // 2 недели

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(dates) { date ->
            val isSelected = date == selectedDate
            val bgColor = if (isSelected) LimePrimary else PureWhite
            val contentColor = if (isSelected) TextBlack else TextGrey

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(bgColor)
                    .clickable { onDateSelected(date) }
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("EEE", Locale("ru"))).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// 2. Карточка занятия (SportUniverse Style)
@Composable
fun ClassCard(yogaClass: YogaClass) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Колонка времени (Слева)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(BackgroundGrey, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = yogaClass.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Text(
                    text = "${yogaClass.durationMinutes} мин",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextGrey
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Информация (Центр)
            Column(modifier = Modifier.weight(1f)) {
                // Тэг типа занятия
                Surface(
                    color = LimeSurface,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = yogaClass.type.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        color = LimeDark,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = yogaClass.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Text(
                    text = yogaClass.teacher.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGrey
                )
            }

            // Кнопка / Статус (Справа)
            if (yogaClass.availableSpots > 0) {
                FilledIconButton(
                    onClick = { /* Логика записи */ },
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = LimePrimary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Записаться", tint = TextBlack)
                }
            } else {
                Text(
                    text = "FULL",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = TextGrey.copy(alpha = 0.5f)
                )
            }
        }
    }
}