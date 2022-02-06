package com.ridill.xpensetracker.feature_expenditures.presentation.add_edit_expenditure

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.navigation.NavArgs
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.use_case.AddEditExpenditureUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditExpenditureViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: AddEditExpenditureUseCases
) : ViewModel(), AddEditExpenditureActions {

    val expenditure = savedStateHandle.getLiveData(KEY_EXPENDITURE_LIVE_DATA, Expenditure.DEFAULT)

    private val expenseId = savedStateHandle.get<Long>(NavArgs.EXPENSE_ID)
    val isEditMode = expenseId != -1L

    private val _showDeleteDialog = savedStateHandle.getLiveData(
        "showDeleteExpenseDialog", false
    )
    val showDeleteExpenseDialog: LiveData<Boolean> = _showDeleteDialog

    private val eventsChannel = Channel<AddEditExpenditureEvents>()
    val events = eventsChannel.receiveAsFlow()

    override fun onNameChange(value: String) {
        expenditure.value = expenditure.value?.copy(name = value)
    }

    override fun onAmountChange(value: String) {
        expenditure.value = expenditure.value?.copy(amount = value.toLongOrNull() ?: 0)
    }

    init {
        if (!savedStateHandle.contains(KEY_EXPENDITURE_LIVE_DATA)) {
            if (expenseId != null && isEditMode) viewModelScope.launch {
                expenditure.value = useCases.getExpenditureById(expenseId)
            } else expenditure.value = Expenditure.DEFAULT
        }
    }

    override fun onSaveClick() {
        viewModelScope.launch {
            expenditure.value?.let {
                when (val response = useCases.saveExpenditure(it)) {
                    is Response.Error -> {
                        eventsChannel.send(
                            AddEditExpenditureEvents.ShowSnackbar(
                                response.message
                                    ?: R.string.error_unknown
                            )
                        )
                    }
                    is Response.Success -> {
                        val result = if (isEditMode) RESULT_EXPENDITURE_UPDATED
                        else RESULT_EXPENDITURE_ADDED
                        eventsChannel.send(AddEditExpenditureEvents.NavigateBackWithResult(result))
                    }
                }
            }
        }
    }

    override fun onRepeatEveryMonthToggle(repeat: Boolean) {
        expenditure.value = expenditure.value?.copy(isMonthly = repeat)
    }

    override fun onDeleteOptionClick() {
        _showDeleteDialog.value = true
    }

    override fun onDeleteDialogDismissed() {
        _showDeleteDialog.value = false
    }

    override fun onDeleteDialogConfirmed() {
        viewModelScope.launch {
            expenditure.value?.let { useCases.deleteExpenditure(it) }
            _showDeleteDialog.value = false
            eventsChannel.send(
                AddEditExpenditureEvents.NavigateBackWithResult(RESULT_EXPENDITURE_DELETED)
            )
        }
    }

    sealed class AddEditExpenditureEvents {
        data class NavigateBackWithResult(val result: String) : AddEditExpenditureEvents()
        data class ShowSnackbar(@StringRes val message: Int) : AddEditExpenditureEvents()
    }
}

private const val KEY_EXPENDITURE_LIVE_DATA = "KEY_EXPENDITURE_LIVE_DATA"

const val ADD_EDIT_EXPENDITURE_RESULT = "ADD_EDIT_EXPENDITURE_RESULT"
const val RESULT_EXPENDITURE_ADDED = "RESULT_EXPENDITURE_ADDED"
const val RESULT_EXPENDITURE_UPDATED = "RESULT_EXPENDITURE_UPDATED"
const val RESULT_EXPENDITURE_DELETED = "RESULT_EXPENDITURE_DELETED"