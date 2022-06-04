package com.ridill.xpensetracker.feature_expenses.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseEntity
import com.ridill.xpensetracker.feature_expenses.data.local.entity.ExpenseTagEntity
import com.ridill.xpensetracker.feature_expenses.domain.model.TagsWithExpensesAndAmount

data class TagsWithExpenses(
    @Embedded
    val tag: ExpenseTagEntity,

    @Relation(
        parentColumn = "name",
        entityColumn = "tag"
    )
    val expenses: List<ExpenseEntity>
) {
    fun toTagsWithExpensesAndAmount(): TagsWithExpensesAndAmount = TagsWithExpensesAndAmount(
        tag = tag.toTag(),
        expenses = expenses.map { it.toExpense() },
        expenseAmount = expenses.sumOf { it.amount }
    )
}