package dev.ridill.xpensetracker.feature_expenses.domain.model

import dev.ridill.xpensetracker.core.ui.util.TextUtil
import dev.ridill.xpensetracker.core.util.DatePatterns

data class MonthStats(
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