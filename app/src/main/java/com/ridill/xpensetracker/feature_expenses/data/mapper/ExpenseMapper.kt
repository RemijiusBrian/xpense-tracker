package com.ridill.xpensetracker.feature_expenses.data.mapper

import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.core.util.Constants
import com.ridill.xpensetracker.core.util.orZero
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import com.ridill.xpensetracker.feature_expenses.data.local.relation.MonthAndExpenditureRelation
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseListItem
import com.ridill.xpensetracker.feature_expenses.domain.model.MonthAndExpenditurePercent

fun MonthAndExpenditureRelation.toMonthAndExpenditurePercent(limit: Long): MonthAndExpenditurePercent =
    MonthAndExpenditurePercent(
        month = month,
        expenditurePercent = expenditure / limit.toFloat()
    )

fun ExpenseEntity.toExpenseListItem(): ExpenseListItem = ExpenseListItem(
    id = id,
    name = name,
    amount = TextUtil.formatAmountWithCurrency(amount),
    date = TextUtil.formatDateWithPattern(dateMillis, Constants.XPENSE_DATE_PATTERN)
)

fun ExpenseEntity.toExpense(): Expense = Expense(
    id = id,
    name = name,
    amount = TextUtil.formatNumber(amount),
    dateMillis = dateMillis,
    monthly = isMonthly,
    tag = tag
)

fun Expense.toEntity(): ExpenseEntity = ExpenseEntity(
    id = id,
    name = name,
    amount = amount.toLongOrNull().orZero(),
    dateMillis = dateMillis,
    isMonthly = monthly,
    tag = tag
)