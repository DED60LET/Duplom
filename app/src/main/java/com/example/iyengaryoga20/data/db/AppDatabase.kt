package com.example.iyengaryoga20.data.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE phoneNumber = :phone")
    suspend fun getUser(phone: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
}

@Dao
interface BookingDao {
    // Получаем записи только конкретного пользователя
    @Query("SELECT * FROM bookings WHERE userPhone = :phone")
    fun getUserBookings(phone: String): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    // Удаляем по ID занятия (так удобнее при клике на расписание)
    @Query("DELETE FROM bookings WHERE classId = :classId AND userPhone = :phone")
    suspend fun deleteBookingByClassId(classId: String, phone: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookings WHERE userPhone = :phone AND classId = :classId)")
    suspend fun isBooked(phone: String, classId: String): Boolean
}

@Database(entities = [UserEntity::class, BookingEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "yoga_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}