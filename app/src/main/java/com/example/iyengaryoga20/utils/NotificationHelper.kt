package com.example.iyengaryoga20.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.iyengaryoga20.R // Убедитесь, что R импортирован правильно
import kotlin.random.Random

object NotificationHelper {
    private const val CHANNEL_ID = "yoga_updates_channel"
    private const val CHANNEL_NAME = "Новости и Напоминания"

    // Список случайных фраз
    private val messages = listOf(
        "🧘‍♀️ Пора на коврик! У вас запланировано занятие.",
        "🔥 Акция: Скидка 15% на индивидуальные занятия!",
        "📅 Новое расписание на следующую неделю уже доступно.",
        "✨ Совет дня: Дышите глубже и держите спину прямо.",
        "📢 Мастер-класс 'Здоровая спина' уже в эту субботу!"
    )

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления о тренировках и новостях"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showRandomNotification(context: Context) {
        // Проверка разрешений для Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                   Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return // Нет разрешения — не показываем
            }
        }

        val randomMessage = messages[Random.nextInt(messages.size)]
        val notificationId = Random.nextInt()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Или ваша иконка R.drawable.ic_launcher_foreground
            .setContentTitle("Iyengar Yoga")
            .setContentText(randomMessage)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}