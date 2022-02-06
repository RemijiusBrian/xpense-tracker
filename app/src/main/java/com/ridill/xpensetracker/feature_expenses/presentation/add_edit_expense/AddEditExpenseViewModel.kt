package com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.navigation.NavArgs
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.use_case.AddEditUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: AddEditUseCases
) : ViewModel(), AddEditExpenseActions {

    val expense = savedStateHandle.getLiveData(KEY_EXPENSE_LIVE_DATA, Expense.DEFAULT)

    private val expenseId = savedStateHandle.get<Long>(NavArgs.EXPENSE_ID)
    val isEditMode = expenseId != -1L

    private val _showDeleteExpenseDialog = savedStateHandle.getLiveData(
        "showDeleteExpenseDialog", false
    )
    val showDeleteExpenseDialog: LiveData<Boolean> = _showDeleteExpenseDialog

    private val eventsChannel = Channel<AddEditEvents>()
    val events = eventsChannel.receiveAsFlow()

    override fun onNameChange(value: String) {
        expense.value = expense.value?.copy(name = value)
    }

    override fun onAmountChange(value: String) {
        expense.value = expense.value?.copy(amount = value.toLongOrNull() ?: 0)
    }

    init {
        if (!savedStateHandle.contains(KEY_EXPENSE_LIVE_DATA)) {
            if (expenseId != null && isEditMode) viewModelScope.launch {
                expense.value = useCases.getExpenseById(expenseId)
                println("AppDebug: Expense - ${expense.value}")
            } else expense.value = Expense.DEFAULT
        }
    }

    override fun onSaveClick() {
        viewModelScope.launch {
            expense.value?.let {
                when (val response = useCases.saveExpense(it)) {
                    is Response.Error -> {
                        eventsChannel.send(
                            AddEditEvents.ShowSnackbar(
                                response.message
                                    ?: R.string.error_unknown
                            )
                        )
                    }
                    is Response.Success -> {
                        val result = if (isEditMode) RESULT_EXPENSE_UPDATED
                        else RESULT_EXPENSE_ADDED
                        eventsChannel.send(AddEditEvents.NavigateBackWithResult(result))
                    }
                }
            }
        }
    }

    override fun onRepeatEveryMonthToggle(repeat: Boolean) {
        expense.value = expense.value?.copy(isMonthly = repeat)
    }

    override fun onDeleteOptionClick() {
        _showDeleteExpenseDialog.value = true
    }

    override fun onDeleteDialogDismissed() {
        _showDeleteExpenseDialog.value = false
    }

    override fun onDeleteDialogConfirmed() {
        viewModelScope.launch {
            expense.value?.let { useCases.deleteExpense(it) }
            _showDeleteExpenseDialog.value = false
            eventsChannel.send(AddEditEvents.NavigateBackWithResult(RESULT_EXPENSE_DELETE))
        }
    }

    sealed class AddEditEvents {
        data class NavigateBackWithResult(val result: String) : AddEditEvents()
        data class ShowSnackbar(@StringRes val message: Int) : AddEditEvents()
    }
}

private const val KEY_EXPENSE_LIVE_DATA = "KEY_EXPENSE_LIVE_DATA"

const val ADD_EDIT_EXPENSE_RESULT = "ADD_EDIT_EXPENSE_RESULT"
const val RESULT_EXPENSE_ADDED = "RESULT_EXPENSE_ADDED"
const val RESULT_EXPENSE_UPDATED = "RESULT_EXPENSE_UPDATED"
const val RESULT_EXPENSE_DELETE = "RESULT_EXPENSE_DELETED"