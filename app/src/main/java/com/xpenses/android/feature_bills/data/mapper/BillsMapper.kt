package com.xpenses.android.feature_bills.data.mapper

import com.xpenses.android.core.ui.util.TextUtil
import com.xpenses.android.core.util.*
import com.xpenses.android.feature_bills.data.local.entity.BillEntity
import com.xpenses.android.feature_bills.data.local.relation.BillWithExpensesRelation
import com.xpenses.android.feature_bills.domain.model.*

fun BillEntity.toBillItem(): BillItem = BillItem(
    id = id,
    name = name,
    category = BillCategory.valueOf(category),
    amount = TextUtil.formatAmountWithCurrency(amount)
)

fun BillEntity.toBill(): Bill = Bill(
    id = id,
    name = name,
    amount = amount.toString(),
    recurring = recurring,
    category = BillCategory.valueOf(category),
    dateMillis = payByDate
)

fun BillWithExpensesRelation.toBillPayment(): BillPayment {
    val expenseForBillInCurrentMonth = expenses.find {
        it.dateMillis.getMonthFromMillis() == getCurrentMonth()
    }
    val state = when {
        expenseForBillInCurrentMonth != null -> BillState.PAID
        bill.payByDate.getDayFromMillis() > getCurrentDay() -> BillState.UPCOMING
        else -> BillState.UNPAID
    }

    return BillPayment(
        id = bill.id,
        paymentOrPayByDate = TextUtil.formatDate(
            expenseForBillInCurrentMonth?.dateMillis ?: bill.payByDate
        ),
        amount = bill.amount,
        category = BillCategory.valueOf(bill.category),
        name = bill.name,
        state = state
    )
}

fun Bill.toEntity(): BillEntity = BillEntity(
    name = name,
    category = category.name,
    payByDate = dateMillis,
    recurring = recurring,
    amount = amount.toDoubleOrZero(),
)