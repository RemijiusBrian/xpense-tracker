package com.ridill.xpensetracker.feature_expenses.presentation.expense_tags

interface TagsActions {
    fun onTagMenuOptionSelect(tagOptions: TagOptions)
    fun onAddTagClick()
    fun onDeleteTagDismiss()
    fun onDeleteTagConfirm()
}