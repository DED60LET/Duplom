package com.example.iyengaryoga20.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.iyengaryoga20.model.YogaClass
import java.time.format.DateTimeFormatter

@Composable
fun YogaCardItem(
    yogaClass: YogaClass,
    onButtonClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        // ЦВЕТ: Берем из темы (в темной теме будет темным, в светлой - белым)
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Время
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    // ЦВЕТ: Фон времени чуть темнее/светлее фона карточки
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp, horizontal = 14.dp)
            ) {
                Text(
                    text = yogaClass.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface // Цвет текста
                )
                Text(
                    text = "${yogaClass.durationMinutes} мин",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Инфо
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer, // Цветной фончик
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = yogaClass.type.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = yogaClass.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = yogaClass.teacher.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Кнопка
            if (yogaClass.isUserBooked) {
                FilledIconButton(onClick = onButtonClick, colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Icon(Icons.Default.Close, contentDescription = "Отменить", tint = MaterialTheme.colorScheme.error)
                }
            } else if (yogaClass.availableSpots > 0) {
                FilledIconButton(onClick = onButtonClick, colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                    Icon(Icons.Default.Add, contentDescription = "Записаться", tint = MaterialTheme.colorScheme.onPrimary)
                }
            } else {
                Text("FULL", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
            }
        }
    }
}