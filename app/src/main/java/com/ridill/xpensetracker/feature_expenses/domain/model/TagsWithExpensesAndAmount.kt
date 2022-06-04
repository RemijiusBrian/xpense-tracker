package com.ridill.xpensetracker.feature_expenses.domain.model

import com.ridill.xpensetracker.core.ui.util.TextUtil

data class TagsWithExpensesAndAmount(
    val tag: ExpenseTag,
    val expenses: List<Expense>,
    val expenseAmount: Long
) {
    val expenseAmountFormatted: String
        get() = "${TextUtil.currencySymbol} ${TextUtil.formatNumber(expenseAmount)}"
}