package dev.ridill.xpensetracker.feature_bills.domain.model

import android.os.Parcelable
import dev.ridill.xpensetracker.core.ui.util.TextUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bill(
    val id: Long,
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
            id = 0L,
            name = "",
            amount = "",
            recurring = true,
            category = BillCategory.MISC,
            dateMillis = System.currentTimeMillis()
        )
    }
}