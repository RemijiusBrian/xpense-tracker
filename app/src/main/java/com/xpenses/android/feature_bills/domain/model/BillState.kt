package com.xpenses.android.feature_bills.domain.model

import androidx.annotation.StringRes
import com.xpenses.android.R

enum class BillState(
    @StringRes val label: Int,
    @StringRes val displayMessage: Int,
) {
    UPCOMING(R.string.label_upcoming_bills, R.string.label_pay_by_date),
    UNPAID(R.string.label_unpaid_bills, R.string.label_pending_since_date),
    PAID(R.string.label_paid_bills, R.string.label_paid_on_date)
}