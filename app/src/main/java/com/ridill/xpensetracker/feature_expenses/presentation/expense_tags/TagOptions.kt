package com.ridill.xpensetracker.feature_expenses.presentation.expense_tags

import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseTag

sealed class TagOptions {
    data class EditTag(val tag: ExpenseTag) : TagOptions()
    data class DeleteTag(val tag: ExpenseTag) : TagOptions()
}