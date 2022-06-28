package dev.ridill.xpensetracker.core.notification

import androidx.core.app.NotificationCompat

interface NotificationHelper<T> {

    fun getBaseNotification(): NotificationCompat.Builder

    fun showNotification(data: T)
}