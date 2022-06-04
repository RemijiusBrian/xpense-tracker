package com.ridill.xpensetracker.feature_expenses.domain.model

import android.os.Parcelable
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseTagEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExpenseTag(
    val name: String,
    val date: Long = System.currentTimeMillis(),
) : Parcelable {
    fun toEntity(): ExpenseTagEntity = ExpenseTagEntity(name)

    companion object {
        val DEFAULT = ExpenseTag(
            name = "",
            date = System.currentTimeMillis()
        )
    }
}