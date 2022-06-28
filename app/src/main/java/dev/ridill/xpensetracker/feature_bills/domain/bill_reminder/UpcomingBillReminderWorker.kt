package dev.ridill.xpensetracker.feature_bills.domain.bill_reminder

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.ridill.xpensetracker.core.notification.NotificationHelper
import dev.ridill.xpensetracker.core.util.DispatcherProvider
import dev.ridill.xpensetracker.feature_bills.domain.model.Bill
import dev.ridill.xpensetracker.feature_bills.domain.repository.BillsRepository
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