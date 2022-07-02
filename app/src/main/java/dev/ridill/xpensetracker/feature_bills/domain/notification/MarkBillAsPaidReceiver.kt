package dev.ridill.xpensetracker.feature_bills.domain.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import dev.ridill.xpensetracker.core.notification.NotificationHelper
import dev.ridill.xpensetracker.core.util.tryOrNull
import dev.ridill.xpensetracker.di.ApplicationScope
import dev.ridill.xpensetracker.feature_bills.data.mapper.toPayment
import dev.ridill.xpensetracker.feature_bills.domain.model.Bill
import dev.ridill.xpensetracker.feature_bills.domain.repository.BillsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MarkBillAsPaidReceiver : BroadcastReceiver() {

    @Inject
    lateinit var billsRepository: BillsRepository

    @ApplicationScope
    @Inject
    lateinit var applicationScope: CoroutineScope

    @Inject
    lateinit var notificationHelper: NotificationHelper<Bill>

    override fun onReceive(context: Context?, intent: Intent?) {
        val data = tryOrNull {
            intent?.extras?.getParcelable<Bill>(KEY_BILL_NOTIFICATION_DATA)
        } ?: return
        applicationScope.launch {
            billsRepository.markBillAsPaid(data.toPayment())
            notificationHelper.dismissNotification(data.id.toInt())
        }
    }
}