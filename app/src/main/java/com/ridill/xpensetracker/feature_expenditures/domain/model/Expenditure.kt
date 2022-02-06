package com.ridill.xpensetracker.feature_expenditures.domain.model

import android.os.Parcelable
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.feature_expenditures.data.local.entity.ExpenditureEntity
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Expenditure(
    val id: Long = 0L,
    val name: String,
    val amount: Long,
    val dateMillis: Long,
    val isMonthly: Boolean,
    val category: ExpenditureCategory
) : Parcelable {

    val dateFormatted: String
        get() = TextUtil.formatDate(dateMillis)

    val amountFormatted: String
        get() = "${Currency.getInstance(Locale.getDefault()).symbol} ${TextUtil.formatNumber(amount)}"

    fun toEntity(): ExpenditureEntity = ExpenditureEntity(
        id = id,
        name = name,
        amount = amount,
        dateMillis = dateMillis,
        isMonthly = isMonthly,
        category = category.name
    )

    companion object {
        val DEFAULT = Expenditure(
            id = 0L,
            name = "",
            amount = 0L,
            dateMillis = System.currentTimeMillis(),
            isMonthly = false,
            category = ExpenditureCategory.EXPENDITURE
        )

        val CASH_FLOW_DEFAULT = Expenditure(
            id = 0L,
            name = "",
            amount = 0L,
            dateMillis = System.currentTimeMillis(),
            isMonthly = false,
            category = ExpenditureCategory.CASH_FLOW
        )
    }
}