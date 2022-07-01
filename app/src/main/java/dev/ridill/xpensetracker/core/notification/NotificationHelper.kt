package dev.ridill.xpensetracker.core.notification

import androidx.core.app.NotificationCompat

interface NotificationHelper<T> {

    fun createNotificationChannel()

    fun getBaseNotification(): NotificationCompat.Builder

    fun showNotification(data: T)

    fun dismissNotification(id: Int)
}