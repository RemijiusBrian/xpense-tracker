package com.ridill.xpensetracker.feature_cash_flow.domain.model

import androidx.annotation.StringRes
import com.ridill.xpensetracker.R

enum class AggregateAmountState(@StringRes val label: Int) {
    IN_DEBT(R.string.in_debt),
    IN_CREDIT(R.string.in_credit),
    BALANCED(R.string.balanced),
    CLEARED(R.string.cleared)
}