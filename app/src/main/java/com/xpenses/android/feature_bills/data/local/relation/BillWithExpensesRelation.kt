package com.xpenses.android.feature_bills.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.xpenses.android.feature_bills.data.local.entity.BillEntity
import com.xpenses.android.feature_expenses.data.local.entity.ExpenseEntity

data class BillWithExpensesRelation(
    @Embedded val bill: BillEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "billId"
    ) val expenses: List<ExpenseEntity>
)