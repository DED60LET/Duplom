package com.example.iyengaryoga20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.iyengaryoga20.components.YogaCardItem
import com.example.iyengaryoga20.model.Teacher
import com.example.iyengaryoga20.model.YogaClass
import com.example.iyengaryoga20.viewmodel.ScheduleViewModel
import java.time.LocalDateTime

@Composable
fun BookingsScreen(viewModel: ScheduleViewModel) {
    // --- ИСПРАВЛЕНИЕ: БЕРЕМ ГОТОВЫЙ СПИСОК ИЗ VIEWMODEL ---
    // Это гарантирует, что список не будет "моргать" или пропадать
    val bookings by viewModel.localBookings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Мои записи",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (bookings.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("У вас пока нет активных записей", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(bookings) { booking ->

                    val safeStartTime = try {
                        LocalDateTime.parse("${booking.date}T${booking.startTime}")
                    } catch (e: Exception) {
                        LocalDateTime.now()
                    }

                    val yogaClass = YogaClass(
                        id = booking.classId,
                        title = booking.className,
                        type = booking.classType,
                        teacher = Teacher(booking.teacherName),
                        startTime = safeStartTime,
                        durationMinutes = 90,
                        totalSpots = 20,
                        bookedSpots = 0,
                        isUserBooked = true,
                        hall = "Студия"
                    )

                    YogaCardItem(
                        yogaClass = yogaClass,
                        onButtonClick = {
                            viewModel.deleteBooking(booking.classId)
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}