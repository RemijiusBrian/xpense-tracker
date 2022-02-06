package com.ridill.xpensetracker.feature_cash_flow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ExpenseEntity::class,
            parentColumns = ["id"],
            childColumns = ["expenseId"]
        )
    ]
)
data class CashFlowEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(index = true)
    val expenseId: Long,
    val name: String,
    val amount: Long,
    val date: Long,
    val lent: Boolean = true,
) {
    fun toCashFlow(): CashFlow = CashFlow(
        id = id,
        expense = expenseId,
        name = name,
        amount = amount,
        date = date,
        lending = lent
    )
}