package com.xpenses.android.feature_bills.domain.model

import com.xpenses.android.core.ui.util.TextUtil

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