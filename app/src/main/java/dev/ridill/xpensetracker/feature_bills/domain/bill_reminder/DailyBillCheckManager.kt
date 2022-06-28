package dev.ridill.xpensetracker.feature_bills.domain.bill_reminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DailyBillCheckManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun scheduleDailyBillCheckManager() {
        val workRequest = PeriodicWorkRequestBuilder<UpcomingBillReminderWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(WORK_ID, ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }
}

private const val WORK_ID = "CHECK_UPCOMING_BILLS"