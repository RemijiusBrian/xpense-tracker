package com.xpenses.android.feature_expenses.data.mapper

import com.xpenses.android.core.ui.util.TextUtil
import com.xpenses.android.core.util.dayWithSuffix
import com.xpenses.android.core.util.orZero
import com.xpenses.android.core.util.toDoubleOrZero
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseEntity
import com.xpenses.android.feature_expenses.data.local.relation.MonthAndExpenditureRelation
import com.xpenses.android.feature_expenses.domain.model.Expense
import com.xpenses.android.feature_expenses.domain.model.ExpenseListItem
import com.xpenses.android.feature_expenses.domain.model.MonthAndExpenditure

fun MonthAndExpenditureRelation.toMonthAndExpenditure(limit: Long): MonthAndExpenditure =
    MonthAndExpenditure(
        month = month,
        expenditureAmount = TextUtil.formatAmountWithCurrency(expenditure),
        expenditurePercent = if (limit > 0) {
            (expenditure / limit.toDouble()).takeIf { !it.isNaN() }.orZero().toFloat()
        } else 0f
    )

fun ExpenseEntity.toExpenseListItem(): ExpenseListItem = ExpenseListItem(
    id = id,
    name = name,
    amount = TextUtil.formatAmountWithCurrency(amount),
    date = dateMillis.dayWithSuffix(longDay = true)
)

fun ExpenseEntity.toExpense(): Expense = Expense(
    id = id,
    name = name,
    amount = amount.toString(),
    dateMillis = dateMillis,
    tag = tag,
    billId = billId
)

fun Expense.toEntity(): ExpenseEntity = ExpenseEntity(
    id = id,
    name = name,
    amount = amount.toDoubleOrZero(),
    dateMillis = dateMillis,
    tag = tag,
    billId = billId
)