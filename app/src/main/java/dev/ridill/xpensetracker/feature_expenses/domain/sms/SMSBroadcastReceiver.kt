package dev.ridill.xpensetracker.feature_expenses.domain.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import dagger.hilt.android.AndroidEntryPoint
import dev.ridill.xpensetracker.core.notification.NotificationHelper
import dev.ridill.xpensetracker.di.ApplicationScope
import dev.ridill.xpensetracker.feature_expenses.domain.model.Expense
import dev.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SMSBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var expenseRepository: ExpenseRepository

    @ApplicationScope
    @Inject
    lateinit var applicationScope: CoroutineScope

    @Inject
    lateinit var notificationHelper: NotificationHelper<Expense>

    @Inject
    lateinit var paymentSmsDataService: PaymentSmsDataService

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || intent.action != "android.provider.Telephony.SMS_RECEIVED") return
        Telephony.Sms.Intents.getMessagesFromIntent(intent).forEach { sms ->
            if (!paymentSmsDataService.isMerchantSms(sms.originatingAddress)) return@forEach

            val body = sms.messageBody
            if (!paymentSmsDataService.isSmsForMonetaryDebit(body)) return@forEach
            val amount = paymentSmsDataService.extractAmount(body) ?: return@forEach
            val date = System.currentTimeMillis()
            val name = buildString {
                append("Payment ")
                append(paymentSmsDataService.extractMerchantName(body))
            }
            addExpense(name, amount, date)
        }
    }

    private fun addExpense(name: String, amount: String, date: Long) = applicationScope.launch {
        val expense = Expense(
            name = name,
            amount = amount,
            dateMillis = date,
            tag = null,
            billId = null
        )
        val insertedId = expenseRepository.cacheExpense(expense)
        notificationHelper.showNotification(expense.copy(id = insertedId))
    }
}