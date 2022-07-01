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

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || intent.action != "android.provider.Telephony.SMS_RECEIVED") return
        Telephony.Sms.Intents.getMessagesFromIntent(intent).forEach { sms ->
            // Check if SMS is transactional
            val sender = sms.originatingAddress ?: return@forEach
            if (!sender.matches(BANK_SENDER_PATTERN.toRegex())) return@forEach
            val body = sms.messageBody.ifEmpty { return@forEach }
            if (!body.contains("debited", ignoreCase = false)) return@forEach

            val amount = AMOUNT_PATTERN.toRegex().find(body)?.value.orEmpty()
                .ifEmpty { return@forEach }
                .let { value ->
                    value.substring(startIndex = value.indexOfFirst { it.isDigit() }).trim()
                }
            val date = System.currentTimeMillis()
            val name = buildString {
                append("Transaction at ")
                append(
                    MERCHANT_PATTERN.toRegex().find(body)?.value.orEmpty().ifEmpty { "Merchant" }
                )
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

private const val BANK_SENDER_PATTERN = "[a-zA-Z0-9]{2}-[a-zA-Z0-9]{6}"
private const val AMOUNT_PATTERN =
    "(?i)(?:RS|INR|MRP)\\.?\\s?(\\d+(:?,\\d+)?(,\\d+)?(\\.\\d{1,2})?)"
private const val MERCHANT_PATTERN =
    "(?i)(?:\\sat\\s|in\\*)([A-Za-z0-9]*\\s?-?\\s?[A-Za-z0-9]*\\s?-?\\.?)"