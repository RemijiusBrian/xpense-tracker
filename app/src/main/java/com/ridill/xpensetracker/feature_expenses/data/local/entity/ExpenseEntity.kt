package com.ridill.xpensetracker.feature_expenses.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory

@Entity
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val amount: Long,
    val date: Long = System.currentTimeMillis(),
    val isMonthly: Boolean = false,
    val category: String,
) {
    fun toExpense(): Expense = Expense(
        id = id,
        name = name,
        amount = amount,
        date = date,
        isMonthly = isMonthly,
        category = ExpenseCategory.valueOf(category)
    )
}