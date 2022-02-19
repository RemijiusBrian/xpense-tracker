package com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.navigation.NavArgs
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: ExpenseRepository
) : ViewModel(), AddEditExpenseActions {

    // Expense
    val expenseLiveData = savedStateHandle.getLiveData<Expense>(KEY_EXPENSE_LIVE_DATA)

    // Expense Arg
    private val expenseId = savedStateHandle.get<Long>(NavArgs.EXPENSE_ID)
    val isEditMode = expenseId != -1L

    private val _showDeleteExpenseDialog = savedStateHandle.getLiveData(
        "showDeleteExpenseDialog", false
    )
    val showDeleteExpenseDialog: LiveData<Boolean> = _showDeleteExpenseDialog

    init {
        if (!savedStateHandle.contains(KEY_EXPENSE_LIVE_DATA)) {
            if (expenseId != null && isEditMode) viewModelScope.launch {
                expenseLiveData.value = repo.getExpenseById(expenseId)
            } else expenseLiveData.value = Expense.DEFAULT
        }
    }

    // Events Channel
    private val eventsChannel = Channel<AddEditExpenseEvents>()
    val events: Flow<AddEditExpenseEvents> = eventsChannel.receiveAsFlow()

    override fun onNameChange(value: String) {
        expenseLiveData.value = expenseLiveData.value?.copy(name = value)
    }

    override fun onAmountChange(value: String) {
        expenseLiveData.value = expenseLiveData.value?.copy(amount = value.toLongOrNull() ?: 0)
    }

    override fun onRepeatEveryMonthToggle(repeat: Boolean) {
        expenseLiveData.value = expenseLiveData.value?.copy(isMonthly = repeat)
    }

    override fun onSaveClick() {
        viewModelScope.launch {
            expenseLiveData.value?.let { expense ->
                val trimmedExpense = expense.copy(name = expense.name.trim())
                if (trimmedExpense.name.isEmpty()) {
                    eventsChannel.send(AddEditExpenseEvents.ShowSnackbar(R.string.error_name_empty))
                    return@launch
                }
                if (expense.amount < 0) {
                    eventsChannel.send(AddEditExpenseEvents.ShowSnackbar(R.string.error_amount_invalid))
                    return@launch
                }
                repo.cacheExpense(trimmedExpense)
                val result = if (isEditMode) RESULT_EXPENSE_UPDATED
                else RESULT_EXPENSE_ADDED
                eventsChannel.send(AddEditExpenseEvents.NavigateBackWithResult(result))
            }
        }
    }

    override fun onDeleteOptionClick() {
        _showDeleteExpenseDialog.value = true
    }

    override fun onDeleteDialogDismissed() {
        _showDeleteExpenseDialog.value = false
    }

    override fun onDeleteDialogConfirmed() {
        viewModelScope.launch {
            expenseLiveData.value?.let { repo.deleteExpense(it) }
            _showDeleteExpenseDialog.value = false
            eventsChannel.send(AddEditExpenseEvents.NavigateBackWithResult(RESULT_EXPENSE_DELETED))
        }
    }

    // Events
    sealed class AddEditExpenseEvents {
        data class NavigateBackWithResult(val result: String) : AddEditExpenseEvents()
        data class ShowSnackbar(@StringRes val message: Int) : AddEditExpenseEvents()
    }
}

private const val KEY_EXPENSE_LIVE_DATA = "KEY_EXPENSE_LIVE_DATA"

const val ADD_EDIT_EXPENSE_RESULT = "ADD_EDIT_EXPENSE_RESULT"
const val RESULT_EXPENSE_ADDED = "RESULT_EXPENSE_ADDED"
const val RESULT_EXPENSE_UPDATED = "RESULT_EXPENSE_UPDATED"
const val RESULT_EXPENSE_DELETED = "RESULT_EXPENSE_DELETED"