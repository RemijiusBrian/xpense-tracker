package com.ridill.xpensetracker.feature_expenses.domain.model

import androidx.annotation.StringRes
import com.ridill.xpensetracker.R

enum class SortCriteria(
    @StringRes val label: Int
) {
    BY_DATE(R.string.date),
    BY_AMOUNT(R.string.amount)
}