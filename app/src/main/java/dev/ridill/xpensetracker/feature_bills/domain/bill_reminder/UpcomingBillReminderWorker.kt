package dev.ridill.xpensetracker.feature_bills.domain.bill_reminder

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.ridill.xpensetracker.core.notification.NotificationHelper
import dev.ridill.xpensetracker.core.util.DispatcherProvider
import dev.ridill.xpensetracker.core.util.getCurrentDay
import dev.ridill.xpensetracker.core.util.getDayFromMillis
import dev.ridill.xpensetracker.feature_bills.domain.model.BillPayment
import dev.ridill.xpensetracker.feature_bills.domain.model.BillState
import dev.ridill.xpensetracker.feature_bills.domain.repository.BillsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlin.math.abs

@HiltWorker
class UpcomingBillReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val billRepo: BillsRepository,
    private val notificationHelper: NotificationHelper<BillPayment>,
    private val dispatcherProvider: DispatcherProvider
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(dispatcherProvider.io) {
        try {
            val payments = billRepo.getBillPaymentsForCurrentMonth().first()
            val upcoming = payments[BillState.UPCOMING] ?: return@withContext Result.failure()

            val billsUpcomingIn3Days = upcoming.filter { entry ->
                val dayDifference = entry.payByDateMillis.getDayFromMillis() - getCurrentDay()
                abs(dayDifference) in 1..3
            }
            billsUpcomingIn3Days.forEach { notificationHelper.showNotification(it) }
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }
}