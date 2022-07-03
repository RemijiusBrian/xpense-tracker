package dev.ridill.xpensetracker.feature_bills.domain.model

import android.os.Parcelable
import dev.ridill.xpensetracker.core.ui.util.TextUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class BillPayment(
    val id: Long,
    val amount: Double,
    val category: BillCategory,
    val name: String,
    val state: BillState,
    val payByDateMillis: Long,
) : Parcelable {
    val amountFormatted: String
        get() = TextUtil.formatAmountWithCurrency(amount)

    val dateFormatted: String
        get() = TextUtil.formatDate(payByDateMillis)
}