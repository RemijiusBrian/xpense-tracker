package dev.ridill.xpensetracker.feature_expenses.domain.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.application.XTActivity
import dev.ridill.xpensetracker.core.notification.NotificationHelper
import dev.ridill.xpensetracker.feature_expenses.domain.model.Expense

class ExpenseNotificationHelper(
    private val applicationContext: Context
) : NotificationHelper<Expense> {

    private val notificationManager = NotificationManagerCompat.from(applicationContext)
    private val pendingIntentFlags =
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

    private fun buildPendingIntentWithId(id: Long): PendingIntent {
        val openExpensesIntent = Intent(
            Intent.ACTION_VIEW,
            "https://www.xpensetracker.ridill.dev/add_edit_expense/$id".toUri(),
            applicationContext,
            XTActivity::class.java
        )
        return PendingIntent.getActivity(
            applicationContext,
            0,
            openExpensesIntent,
            pendingIntentFlags
        )
    }

    init {
        createNotificationChannel()
    }

    override fun createNotificationChannel() {
        val billReminderChannel = NotificationChannelCompat.Builder(
            EXPENSE_NOTIFICATION_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(applicationContext.getString(R.string.bills_reminder_notification_channel_name))
            .setDescription(applicationContext.getString(R.string.bills_reminder_notification_channel_description))
            .build()

        notificationManager.createNotificationChannelsCompat(listOf(billReminderChannel))
    }

    override fun getBaseNotification(): NotificationCompat.Builder =
        NotificationCompat.Builder(applicationContext, EXPENSE_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_payment)
            .setContentTitle(applicationContext.getString(R.string.expenses_notification_expense_added))
            .setGroup(EXPENSE_NOTIFICATION_GROUP)

    override fun showNotification(data: Expense) {
        val notification = getBaseNotification()
            .setContentText(
                applicationContext.getString(
                    R.string.expenses_notification_expense_added_for_payment,
                    data.name
                )
            )
            .setContentIntent(buildPendingIntentWithId(data.id))
            .build()
        val summary = buildSummaryNotification().build()
        notificationManager.apply {
            notify(data.id.toInt(), notification)
            notify(EXPENSE_SUMMARY_ID, summary)
        }
    }

    override fun dismissNotification(id: Int) {
        notificationManager.cancel(EXPENSE_SUMMARY_ID)
    }

    private fun buildSummaryNotification(): NotificationCompat.Builder =
        getBaseNotification()
            .setStyle(
                NotificationCompat.InboxStyle()
                    .setSummaryText(applicationContext.getString(R.string.expenses_added_notification_summary))
            )
            .setGroupSummary(true)

}

private const val EXPENSE_NOTIFICATION_CHANNEL_ID = "expense_notification_channel"
private const val EXPENSE_NOTIFICATION_GROUP = "expense_group"
private const val EXPENSE_SUMMARY_ID = 2