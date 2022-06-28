package dev.ridill.xpensetracker.feature_bills.domain.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.application.XTActivity
import dev.ridill.xpensetracker.core.notification.NotificationHelper
import dev.ridill.xpensetracker.feature_bills.domain.model.Bill

class BillReminderNotificationHelper(
    private val applicationContext: Context
) : NotificationHelper<Bill> {

    private val notificationManager = NotificationManagerCompat.from(applicationContext)
    private val pendingIntentFlags =
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

    private val openBillsIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.xpensetracker.ridill.dev/bills_list".toUri(),
        applicationContext,
        XTActivity::class.java
    )
    private val openBillsPendingIntent =
        PendingIntent.getActivity(applicationContext, 0, openBillsIntent, pendingIntentFlags)

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val billReminderChannel = NotificationChannelCompat.Builder(
            BILL_REMINDER_NOTIFICATION_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(applicationContext.getString(R.string.bills_reminder_notification_channel_name))
            .setDescription(applicationContext.getString(R.string.bills_reminder_notification_channel_description))
            .build()

        notificationManager.createNotificationChannelsCompat(listOf(billReminderChannel))
    }

    override fun getBaseNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, BILL_REMINDER_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_receipt_long)
            .setContentIntent(openBillsPendingIntent)
            .setGroup(BILL_REMINDER_GROUP)
    }

    override fun showNotification(data: Bill) {
        val notification = getBaseNotification()
            .setContentTitle(data.name)
            .setContentText(
                applicationContext.getString(
                    R.string.bill_reminder_notification_text,
                    data.dateFormatted
                )
            )
            .addAction(
                R.drawable.ic_receipt_long,
                applicationContext.getString(R.string.mark_as_paid),
                buildActionPendingIntent(data)
            )
            .build()
        val summary = buildSummaryNotification().build()
        notificationManager.apply {
            notify(data.id.toInt(), notification)
            notify(BILL_REMINDER_SUMMARY_ID, summary)
        }
    }

    private fun buildSummaryNotification(): NotificationCompat.Builder {
        val inboxStyle = NotificationCompat.InboxStyle()
        return getBaseNotification()
            .setStyle(inboxStyle)
            .setGroupSummary(true)
    }

    private fun buildActionPendingIntent(data: Bill): PendingIntent {
        val actionIntent = Intent(applicationContext, MarkBillAsPaidReceiver::class.java).apply {
            putExtras(
                bundleOf(
                    KEY_BILL_DATA to data
                )
            )
        }
        return PendingIntent.getBroadcast(
            applicationContext,
            data.id.toInt(),
            actionIntent,
            pendingIntentFlags
        )
    }
}

const val KEY_BILL_DATA = "bill"
private const val BILL_REMINDER_NOTIFICATION_CHANNEL_ID = "bill_reminder_notification_channel"
private const val BILL_REMINDER_GROUP = "bill_reminder_group"
private const val BILL_REMINDER_SUMMARY_ID = 1