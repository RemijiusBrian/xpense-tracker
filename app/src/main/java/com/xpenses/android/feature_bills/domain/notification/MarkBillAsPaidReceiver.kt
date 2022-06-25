package com.xpenses.android.feature_bills.domain.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.xpenses.android.core.util.toDoubleOrZero
import com.xpenses.android.core.util.tryOrNull
import com.xpenses.android.di.ApplicationScope
import com.xpenses.android.feature_bills.domain.model.Bill
import com.xpenses.android.feature_bills.domain.model.BillPayment
import com.xpenses.android.feature_bills.domain.model.BillState
import com.xpenses.android.feature_bills.domain.notification.KEY_BILL_DATA
import com.xpenses.android.feature_bills.domain.repository.BillsRepository
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onReceive(context: Context?, intent: Intent?) {
        val data = tryOrNull { intent?.extras?.get(KEY_BILL_DATA) as? Bill } ?: return

        applicationScope.launch {
            billsRepository.markBillAsPaid(
                BillPayment(
                    id = data.id,
                    paymentOrPayByDate = data.dateFormatted,
                    amount = data.amount.toDoubleOrZero(),
                    category = data.category,
                    name = data.name,
                    state = BillState.PAID
                )
            )
        }
    }
}