package com.example.iyengaryoga20.model

import java.time.LocalDateTime
import java.util.UUID

data class Teacher(
    val name: String,
    val photoUrl: String? = null
)

data class YogaClass(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val type: String,
    val teacher: Teacher,
    val startTime: LocalDateTime,
    val durationMinutes: Int,
    val totalSpots: Int,
    val bookedSpots: Int,
    val isUserBooked: Boolean = false,
    val hall: String = "Большой зал" // НОВОЕ ПОЛЕ
) {
    val availableSpots: Int get() = totalSpots - bookedSpots
    val endTime: LocalDateTime get() = startTime.plusMinutes(durationMinutes.toLong())
}