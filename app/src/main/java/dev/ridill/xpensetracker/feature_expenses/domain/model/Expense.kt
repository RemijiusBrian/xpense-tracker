package dev.ridill.xpensetracker.feature_expenses.domain.model

import android.os.Parcelable
import dev.ridill.xpensetracker.core.ui.util.TextUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense(
    val id: Long = 0L,
    val name: String,
    val amount: String,
    val dateMillis: Long,
    val tag: String?,
    val billId: Long?
) : Parcelable {

    val dateFormatted: String
        get() = TextUtil.formatDate(dateMillis)

    companion object {
        val DEFAULT = Expense(
            id = 0L,
            name = "",
            amount = "",
            dateMillis = System.currentTimeMillis(),
            tag = null,
            billId = null
        )
    }
}