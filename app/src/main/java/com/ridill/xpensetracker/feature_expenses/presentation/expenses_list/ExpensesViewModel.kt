package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: ExpenseRepository,
    private val preferencesManager: XTPreferencesManager
) : ViewModel(), ExpensesActions {

    private val preferences = preferencesManager.preferences

    private val dates = repo.getDates()

    override fun onExpenseClick(expense: Expense) {
        TODO("Not yet implemented")
    }

    override fun onAddExpenseClick() {
        TODO("Not yet implemented")
    }

    override fun onExpenseSwipeDeleted(expense: Expense) {
        TODO("Not yet implemented")
    }

    override fun onDateSelect(date: String) {
        TODO("Not yet implemented")
    }

    override fun onEditExpenditureLimitClick() {
        TODO("Not yet implemented")
    }

    override fun onExpenditureLimitUpdateDismiss() {
        TODO("Not yet implemented")
    }

    override fun onExpenditureLimitUpdateConfirm(limit: String) {
        TODO("Not yet implemented")
    }
}