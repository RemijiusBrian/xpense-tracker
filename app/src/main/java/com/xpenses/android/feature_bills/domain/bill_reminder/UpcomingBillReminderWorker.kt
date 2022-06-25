package com.xpenses.android.feature_bills.domain.bill_reminder

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.xpenses.android.core.notification.NotificationHelper
import com.xpenses.android.core.util.DispatcherProvider
import com.xpenses.android.feature_bills.domain.model.Bill
import com.xpenses.android.feature_bills.domain.repository.BillsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

@HiltWorker
class UpcomingBillReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val billRepo: BillsRepository,
    private val notificationHelper: NotificationHelper<Bill>,
    private val dispatcherProvider: DispatcherProvider
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(dispatcherProvider.io) {
        try {
            val currentDate = Date()
            val bills = billRepo.getAllBillsList()
            val upcomingBills = bills.filter {
                val difference = it.dateMillis - currentDate.time
                difference < TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
            }
            upcomingBills.forEach { notificationHelper.showNotification(it) }
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }
}