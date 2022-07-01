package dev.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense

import androidx.lifecycle.*
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.domain.model.UiText
import dev.ridill.xpensetracker.core.notification.NotificationHelper
import dev.ridill.xpensetracker.core.ui.navigation.screen_specs.AddEditExpenseScreenSpec
import dev.ridill.xpensetracker.core.util.Constants
import dev.ridill.xpensetracker.core.util.toDoubleOrZero
import dev.ridill.xpensetracker.feature_expenses.domain.model.Expense
import dev.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: ExpenseRepository,
    private val notificationHelper: NotificationHelper<Expense>
) : ViewModel(), AddEditExpenseActions {

    private val expenseId =
        AddEditExpenseScreenSpec.getExpenseIdFromSavedStateHandle(savedStateHandle)
    val editMode = AddEditExpenseScreenSpec.isNewExpenseMode(expenseId)

    private val expenseInput = savedStateHandle.getLiveData<Expense>(KEY_EXPENSE_LIVE_DATA)
    val amount = expenseInput.map { it.amount }
    val name = expenseInput.map { it.name }
    private val tag = expenseInput.map { it.tag }.distinctUntilChanged()

    private val isBillExpense = expenseInput.map {
        it.billId != null
    }

    private val tagInputExpanded = savedStateHandle.getLiveData("newTagModeActive", false)

    private val showDeleteConfirmation =
        savedStateHandle.getLiveData("showDeleteConfirmation", false)

    private val tagsList = repo.getTagsList()

    init {
        if (!savedStateHandle.contains(KEY_EXPENSE_LIVE_DATA)) {
            if (expenseId != null && editMode) viewModelScope.launch {
                notificationHelper.dismissNotification(expenseId.toInt())
                expenseInput.value = repo.getExpenseById(expenseId)
            } else {
                expenseInput.value = Expense.DEFAULT
            }
        }
    }

    private val eventsChannel = Channel<AddEditEvents>()
    val events get() = eventsChannel.receiveAsFlow()

    val state = combineTuple(
        tagsList,
        tag.asFlow(),
        tagInputExpanded.asFlow(),
        showDeleteConfirmation.asFlow(),
        isBillExpense.asFlow()
    ).map { (
                tagsList,
                tag,
                tagInputExpanded,
                showDeleteConfirmation,
                isBillExpense
            ) ->
        AddEditExpenseState(
            tagsList = tagsList,
            selectedTag = tag,
            tagInputExpanded = tagInputExpanded,
            showDeleteConfirmation = showDeleteConfirmation,
            isBillExpense = isBillExpense
        )
    }.asLiveData()

    override fun onAmountChange(value: String) {
        expenseInput.value = expenseInput.value?.copy(
            amount = value
        )
    }

    override fun onNameChange(value: String) {
        if (value.length > Constants.EXPENSE_NOTE_MAX_LENGTH) return
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
        tagInputExpanded.value = tagInputExpanded.value?.not()
    }

    private val _newTagInput = savedStateHandle.getLiveData("newTagInput", "")
    val newTagInput: LiveData<String> = _newTagInput
    override fun onNewTagValueChange(value: String) {
        if (value.length > Constants.TAG_NAME_MAX_LENGTH) return
        _newTagInput.value = value
    }

    override fun onNewTagInputDismiss() {
        dismissTagInput()
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
            dismissTagInput()
            eventsChannel.send(AddEditEvents.ShowSnackbar(UiText.StringResource(R.string.tag_created)))
        }
    }

    private fun dismissTagInput() {
        _newTagInput.value = ""
        tagInputExpanded.value = false
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
                eventsChannel.send(AddEditEvents.ShowSnackbar(UiText.StringResource(R.string.error_invalid_note)))
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