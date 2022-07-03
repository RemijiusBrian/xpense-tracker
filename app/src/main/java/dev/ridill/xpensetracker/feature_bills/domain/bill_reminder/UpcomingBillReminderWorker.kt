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
            val unpaidBills = billRepo.getBillPaymentsForCurrentMonth().first()
                .filter { it.state != BillState.PAID }
            val billsUpcomingIn3Days = unpaidBills.filter { payment ->
                val dayDifference = payment.payByDateMillis.getDayFromMillis() - getCurrentDay()
                dayDifference in 1..3
            }
            billsUpcomingIn3Days.forEach { notificationHelper.showNotification(it) }
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }
}