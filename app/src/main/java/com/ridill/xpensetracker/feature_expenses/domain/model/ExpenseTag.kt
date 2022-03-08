package com.ridill.xpensetracker.feature_expenses.domain.model

import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseTagEntity

data class ExpenseTag(
    val name: String
) {
    fun toEntity(): ExpenseTagEntity = ExpenseTagEntity(name)
}