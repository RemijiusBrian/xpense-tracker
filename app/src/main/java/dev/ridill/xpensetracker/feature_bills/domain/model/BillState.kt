package dev.ridill.xpensetracker.feature_bills.domain.model

import androidx.annotation.StringRes
import dev.ridill.xpensetracker.R

enum class BillState(
    @StringRes val label: Int,
    @StringRes val displayMessage: Int,
) {
    UPCOMING(R.string.label_upcoming_bills, R.string.pay_by_date_value),
    UNPAID(R.string.label_unpaid_bills, R.string.pending_since_date_value),
    PAID(R.string.label_paid_bills, R.string.paid_on_date_value)
}