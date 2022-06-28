package dev.ridill.xpensetracker.feature_bills.domain.model

import dev.ridill.xpensetracker.core.ui.util.TextUtil

data class BillPayment(
    val id: Long,
    val paymentOrPayByDate: String,
    val amount: Double,
    val category: BillCategory,
    val name: String,
    val state: BillState
) {
    val amountFormatted: String
        get() = TextUtil.formatAmountWithCurrency(amount)
}