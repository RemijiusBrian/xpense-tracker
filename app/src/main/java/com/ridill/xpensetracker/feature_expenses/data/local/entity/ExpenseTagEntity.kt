package com.ridill.xpensetracker.feature_expenses.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseTag

@Entity
data class ExpenseTagEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String
) {
    fun toTag(): ExpenseTag = ExpenseTag(name, )
}