package com.ridill.xpensetracker.feature_dashboard.domain.model

import android.os.Parcelable
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Expense(
    val id: Long = 0L,
    val name: String,
    val amount: Long,
    val dateMillis: Long,
    val isMonthly: Boolean,
    val category: ExpenseCategory
) : Parcelable {
    val date: Date get() = Date(dateMillis)

    val dateFormatted: String
        get() = TextUtil.formatDate(date)

    val amountFormatted: String
        get() = "${Currency.getInstance(Locale.getDefault()).symbol} ${TextUtil.formatNumber(amount)}"

    fun toEntity(): ExpenseEntity = ExpenseEntity(
        id = id,
        name = name,
        amount = amount,
        date = date.time,
        isMonthly = isMonthly,
        category = category.name
    )

    companion object {
        val DEFAULT = Expense(
            id = 0L,
            name = "",
            amount = 0L,
            dateMillis = System.currentTimeMillis(),
            isMonthly = false,
            category = ExpenseCategory.EXPENSE
        )

        val CASH_FLOW_DEFAULT = Expense(
            id = 0L,
            name = "",
            amount = 0L,
            dateMillis = System.currentTimeMillis(),
            isMonthly = false,
            category = ExpenseCategory.CASH_FLOW
        )
    }
}