package com.xpenses.android.feature_expenses.presentation.add_edit_expense

import androidx.lifecycle.*
import com.xpenses.android.R
import com.xpenses.android.core.domain.model.UiText
import com.xpenses.android.core.ui.navigation.screen_specs.AddEditExpenseScreenSpec
import com.xpenses.android.core.util.toDoubleOrZero
import com.xpenses.android.feature_expenses.domain.model.Expense
import com.xpenses.android.feature_expenses.domain.repository.ExpenseRepository
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: ExpenseRepository
) : ViewModel(), AddEditExpenseActions {

    private val expenseId =
        AddEditExpenseScreenSpec.getExpenseIdFromSavedStateHandle(savedStateHandle)
    val editMode = AddEditExpenseScreenSpec.isEditMode(expenseId)

    private val expenseInput = savedStateHandle.getLiveData<Expense>(KEY_EXPENSE_LIVE_DATA)
    val amount = expenseInput.map { it.amount }
    val name = expenseInput.map { it.name }

    private val tagInputExpanded = savedStateHandle.getLiveData("newTagModeActive", false)

    private val showDeleteConfirmation =
        savedStateHandle.getLiveData("showDeleteConfirmation", false)

    private val tagsList = repo.getTagsList()

    init {
        if (!savedStateHandle.contains(KEY_EXPENSE_LIVE_DATA)) {
            if (expenseId != null && editMode) viewModelScope.launch {
                expenseInput.value = repo.getExpenseById(expenseId)
            } else {
                expenseInput.value = Expense.DEFAULT
            }
        }
    }

    private val eventsChannel = Channel<AddEditEvents>()
    val events get() = eventsChannel.receiveAsFlow()

    val state = combineTuple(
        expenseInput.asFlow(),
        tagsList,
        tagInputExpanded.asFlow(),
        showDeleteConfirmation.asFlow()
    ).map { (
                expense,
                tagsList,
                tagInputExpanded,
                showDeleteConfirmation
            ) ->
        AddEditExpenseState(
            expense = expense,
            tagsList = tagsList,
            tagInputExpanded = tagInputExpanded,
            showDeleteConfirmation = showDeleteConfirmation
        )
    }.asLiveData()

    override fun onAmountChange(value: String) {
        expenseInput.value = expenseInput.value?.copy(
            amount = value
        )
    }

    override fun onNameChange(value: String) {
        expenseInput.value = expenseInput.value?.copy(
            name = value
        )
    }

    override fun onTagSelect(tag: String) {
        val newTag = tag.takeIf { it != expenseInput.value?.tag }
        expenseInput.value = expenseInput.value?.copy(
            tag = newTag
        )
    }

    override fun onNewTagClick() {
        tagInputExpanded.value = true
    }

    private val _newTagInput = savedStateHandle.getLiveData("newTagInput", "")
    val newTagInput: LiveData<String> = _newTagInput
    override fun onNewTagValueChange(value: String) {
        _newTagInput.value = value
    }

    override fun onNewTagInputDismiss() {
        tagInputExpanded.value = false
    }

    override fun onNewTagConfirm() {
        val tag = newTagInput.value?.trim().orEmpty()
        viewModelScope.launch {
            if (tag.isEmpty()) {
                eventsChannel.send(AddEditEvents.ShowSnackbar(UiText.StringResource(R.string.error_invalid_tag_name)))
                return@launch
            }
            repo.cacheTag(tag)
            expenseInput.value = expenseInput.value?.copy(
                tag = tag
            )
            _newTagInput.value = ""
            tagInputExpanded.value = false
            eventsChannel.send(AddEditEvents.ShowSnackbar(UiText.StringResource(R.string.tag_created)))
        }
    }

    override fun onDeleteClick() {
        showDeleteConfirmation.value = true
    }

    override fun onDeleteDismiss() {
        showDeleteConfirmation.value = false
    }

    override fun onDeleteConfirm() {
        expenseId?.let { id ->
            viewModelScope.launch {
                repo.deleteExpenseById(id)
                showDeleteConfirmation.value = false
                eventsChannel.send(AddEditEvents.ExpenseDeleted)
            }
        }
    }

    override fun onSave() {
        viewModelScope.launch {
            val amount = amount.value?.trim().orEmpty()
            if (amount.isEmpty() || amount.toDoubleOrZero() <= 0.0) {
                eventsChannel.send(AddEditEvents.ShowSnackbar(UiText.StringResource(R.string.error_invalid_amount)))
                return@launch
            }
            val name = name.value?.trim().orEmpty()
            if (name.isEmpty()) {
                eventsChannel.send(AddEditEvents.ShowSnackbar(UiText.StringResource(R.string.error_invalid_name)))
                return@launch
            }
            expenseInput.value?.let { expense ->
                repo.cacheExpense(expense)
                eventsChannel.send(
                    if (editMode) AddEditEvents.ExpenseUpdated
                    else AddEditEvents.ExpenseCreated
                )
            }
        }
    }

    sealed class AddEditEvents {
        object ExpenseCreated : AddEditEvents()
        object ExpenseUpdated : AddEditEvents()
        object ExpenseDeleted : AddEditEvents()
        data class ShowSnackbar(val message: UiText) : AddEditEvents()
    }
}

private const val KEY_EXPENSE_LIVE_DATA = "KEY_EXPENSE_LIVE_DATA"

const val ADD_EDIT_EXPENSE_RESULT = "ADD_EDIT_EXPENSE_RESULT"
const val RESULT_EXPENSE_ADDED = "RESULT_EXPENSE_ADDED"
const val RESULT_EXPENSE_UPDATED = "RESULT_EXPENSE_UPDATED"
const val RESULT_EXPENSE_DELETED = "RESULT_EXPENSE_DELETED"