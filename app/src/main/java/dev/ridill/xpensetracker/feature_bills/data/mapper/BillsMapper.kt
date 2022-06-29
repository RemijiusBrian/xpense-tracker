package dev.ridill.xpensetracker.feature_bills.data.mapper

import dev.ridill.xpensetracker.core.ui.util.TextUtil
import dev.ridill.xpensetracker.core.util.*
import dev.ridill.xpensetracker.feature_bills.data.local.entity.BillEntity
import dev.ridill.xpensetracker.feature_bills.data.local.relation.BillWithExpensesRelation
import dev.ridill.xpensetracker.feature_bills.domain.model.*

fun BillEntity.toBillItem(): BillItem = BillItem(
    id = id,
    name = name,
    category = BillCategory.valueOf(category),
    amount = TextUtil.formatAmountWithCurrency(amount),
    payBy = TextUtil.formatDateWithPattern(payByDate, DatePatterns.DAY_SHORT_MONTH_NAME_YEAR)
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
        bill.payByDate.getDayFromMillis() >= getCurrentDay() -> BillState.UPCOMING
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
    id = id,
    name = name,
    category = category.name,
    payByDate = dateMillis,
    recurring = recurring,
    amount = amount.toDoubleOrZero()
)

fun Bill.toPayment(): BillPayment = BillPayment(
    id = id,
    paymentOrPayByDate = dateFormatted,
    amount = amount.toDoubleOrZero(),
    category = category,
    name = name,
    state = BillState.PAID
)