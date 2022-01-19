package com.ridill.xpensetracker.feature_cash_flow.domain.model

import android.os.Parcelable
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.feature_cash_flow.data.local.entity.CashFlowEntity
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class CashFlow(
    val id: Long,
    val expense: Long,
    val name: String,
    val amount: Long,
    private val date: Long,
    val lending: Boolean = true,
) : Parcelable {
    val dateFormatted: String
        get() = TextUtil.formatDate(date)

    val amountFormatted: String
        get() = "${Currency.getInstance(Locale.getDefault()).symbol} ${TextUtil.formatNumber(amount)}"

    fun toEntity(): CashFlowEntity = CashFlowEntity(
        id = id,
        expenseId = expense,
        name = name,
        amount = amount,
        date = date,
        lent = lending
    )

    companion object {
        fun default(expenseId: Long): CashFlow = CashFlow(
            id = 0L,
            expense = expenseId,
            name = "",
            amount = 0L,
            date = System.currentTimeMillis(),
            lending = true
        )
    }
}