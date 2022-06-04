package com.ridill.xpensetracker.feature_expenses.presentation.expense_tags

import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseTag
import com.ridill.xpensetracker.feature_expenses.domain.model.TagsWithExpensesAndAmount

data class TagsListState(
    val tagsWithExpense: List<TagsWithExpensesAndAmount> = emptyList(),
    val showDeleteTagDialog: Boolean = false,
    val activeTag: ExpenseTag? = null
) {
    companion object {
        val INITIAL = TagsListState()
    }
}