package com.example.iyengaryoga20.data.db

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE phoneNumber = :phone")
    suspend fun getUser(phone: String): UserEntity?

    @Query("SELECT * FROM users WHERE phoneNumber = :phone")
    fun getUserFlow(phone: String): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // <--- ДОБАВЛЯЕМ МЕТОД ОБНОВЛЕНИЯ ПОЛЬЗОВАТЕЛЯ --->
    @Update
    suspend fun updateUser(user: UserEntity)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings WHERE userPhone = :phone")
    fun getUserBookings(phone: String): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("DELETE FROM bookings WHERE classId = :classId AND userPhone = :phone")
    suspend fun deleteBookingByClassId(classId: String, phone: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookings WHERE userPhone = :phone AND classId = :classId)")
    suspend fun isBooked(phone: String, classId: String): Boolean
}

@Database(entities = [UserEntity::class, BookingEntity::class], version = 2, exportSchema = false)
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
                )
                    .fallbackToDestructiveMigration() // <--- ЭТО СПАСЕТ ОТ ОШИБОК ПРИ СМЕНЕ ТАБЛИЦЫ
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}