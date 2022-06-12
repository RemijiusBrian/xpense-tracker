package com.ridill.xpensetracker.feature_expenses.domain.model

import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.core.util.Constants

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