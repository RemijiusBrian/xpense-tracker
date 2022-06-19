package com.xpenses.android.feature_bills.domain.model

import android.os.Parcelable
import com.xpenses.android.core.ui.util.TextUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bill(
    val name: String,
    val amount: String,
    val recurring: Boolean,
    val category: BillCategory,
    val dateMillis: Long
) : Parcelable {

    val dateFormatted: String
        get() = TextUtil.formatDate(dateMillis)

    companion object {
        val DEFAULT = Bill(
            name = "",
            amount = "",
            recurring = true,
            category = BillCategory.WATER,
            dateMillis = System.currentTimeMillis()
        )
    }
}