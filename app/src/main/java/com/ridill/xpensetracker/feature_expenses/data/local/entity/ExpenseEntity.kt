package com.ridill.xpensetracker.feature_expenses.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense

@Entity
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val amount: Long,
    val dateMillis: Long,
    val isMonthly: Boolean,
) {
    fun toExpense(): Expense = Expense(
        id = id,
        name = name,
        amount = amount,
        dateMillis = dateMillis,
        isMonthly = isMonthly,
    )
}