package dev.ridill.xpensetracker.feature_expenses.data.mapper

import dev.ridill.xpensetracker.core.ui.util.TextUtil
import dev.ridill.xpensetracker.core.util.dayWithSuffix
import dev.ridill.xpensetracker.core.util.orZero
import dev.ridill.xpensetracker.core.util.toDoubleOrZero
import dev.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import dev.ridill.xpensetracker.feature_expenses.data.local.relation.MonthAndExpenditureRelation
import dev.ridill.xpensetracker.feature_expenses.domain.model.Expense
import dev.ridill.xpensetracker.feature_expenses.domain.model.ExpenseListItem
import dev.ridill.xpensetracker.feature_expenses.domain.model.MonthStats

fun MonthAndExpenditureRelation.toMonthAndExpenditure(limit: Long): MonthStats = MonthStats(
    month = month,
    expenditureAmount = TextUtil.compactFormatAmountWithCurrency(expenditure),
    expenditurePercent = if (limit > 0) {
        (expenditure / limit.toDouble()).takeIf { !it.isNaN() }.orZero().toFloat()
    } else 0f,
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