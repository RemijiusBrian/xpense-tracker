package com.xpenses.android.feature_bills.data.mapper

import com.xpenses.android.core.ui.util.TextUtil
import com.xpenses.android.feature_bills.data.local.entity.BillEntity
import com.xpenses.android.feature_bills.data.local.relation.BillWithExpensesRelation
import com.xpenses.android.feature_bills.domain.model.BillCategory
import com.xpenses.android.feature_bills.domain.model.BillItem
import com.xpenses.android.feature_bills.domain.model.BillPayment
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseEntity

fun BillEntity.toBillItem(): BillItem = BillItem(
    id = id,
    name = name,
    category = BillCategory.valueOf(category),
    amount = TextUtil.formatAmountWithCurrency(amount)
)

fun BillWithExpensesRelation.toBillWithPayments(): BillItem = BillItem(
    id = bill.id,
    name = bill.name,
    category = BillCategory.valueOf(bill.category),
    amount = TextUtil.formatAmountWithCurrency(bill.amount),
)

fun ExpenseEntity.toBillPayment(): BillPayment = BillPayment(
    dateMillis = dateMillis
)