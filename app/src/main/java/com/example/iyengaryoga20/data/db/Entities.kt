package com.example.iyengaryoga20.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val phoneNumber: String,
    val fullName: String,
    val email: String,
    val birthDate: String,
    val cardId: String = ""
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userPhone: String, // Привязка к пользователю
    val classId: String,   // ID занятия из API
    val className: String,
    val classType: String,
    val teacherName: String,
    val date: String,
    val startTime: String
)