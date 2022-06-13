package com.xpenses.android.feature_expenses.domain.model

import com.xpenses.android.core.ui.util.TextUtil
import com.xpenses.android.core.util.Constants

data class MonthAndExpenditurePercent(
    val month: String,
    val expenditurePercent: Float
) {
    val monthFormatted: String
        get() = TextUtil.run {
            parseDate(month, Constants.MONTH_YEAR_DB_PATTERN)?.let {
                formatDateWithPattern(it.time, Constants.MONTH_YEAR_UI_PATTERN)
            }.orEmpty()
        }
}