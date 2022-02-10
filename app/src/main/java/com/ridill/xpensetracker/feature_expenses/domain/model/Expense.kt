package com.ridill.xpensetracker.feature_expenses.domain.model

import android.os.Parcelable
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Expense(
    val id: Long = 0L,
    val name: String,
    val amount: Long,
    val dateMillis: Long,
    val isMonthly: Boolean,
) : Parcelable {

    val dateFormatted: String
        get() = SimpleDateFormat("dd, EEEE", Locale.getDefault()).format(Date(dateMillis))

    val amountFormatted: String
        get() = "${Currency.getInstance(Locale.getDefault()).symbol} ${TextUtil.formatNumber(amount)}"

    fun toEntity(): ExpenseEntity = ExpenseEntity(
        id = id,
        name = name,
        amount = amount,
        dateMillis = dateMillis,
        isMonthly = isMonthly,
    )

    companion object {
        val DEFAULT = Expense(
            id = 0L,
            name = "",
            amount = 0L,
            dateMillis = System.currentTimeMillis(),
            isMonthly = false,
        )
    }
}