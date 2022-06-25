package com.xpenses.android.core.notification

import androidx.core.app.NotificationCompat

interface NotificationHelper<T> {

    fun getBaseNotification(): NotificationCompat.Builder

    fun showNotification(data: T)
}