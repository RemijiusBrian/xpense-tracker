package com.xpenses.android.feature_expenses.domain.model

import com.xpenses.android.core.ui.util.TextUtil
import com.xpenses.android.core.util.DatePatterns

data class MonthAndExpenditure(
    val month: String,
    val expenditureAmount: String,
    val expenditurePercent: Float
) {
    val monthFormatted: String
        get() = TextUtil.run {
            parseDate(month, DatePatterns.MONTH_NUMBER_WITH_YEAR)?.let {
                formatDateWithPattern(it.time, DatePatterns.SHORT_MONTH_NAME_WITH_YEAR)
            }.orEmpty()
        }
}