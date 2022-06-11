package com.ridill.xpensetracker.feature_expenses.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Expense(
    val id: Long = 0L,
    val name: String,
    val amount: String,
    val dateMillis: Long,
    val monthly: Boolean,
    val tag: String?
) : Parcelable {

    companion object {
        val DEFAULT = Expense(
            id = 0L,
            name = "",
            amount = "",
            dateMillis = System.currentTimeMillis(),
            monthly = false,
            tag = null
        )
    }
}