package com.xpenses.android.feature_expenses.presentation.expenses_list

import androidx.lifecycle.*
import com.xpenses.android.R
import com.xpenses.android.core.data.preferences.XTPreferencesManager
import com.xpenses.android.core.domain.model.UiText
import com.xpenses.android.core.ui.navigation.screen_specs.AddEditExpenseScreenSpec
import com.xpenses.android.core.ui.util.TextUtil
import com.xpenses.android.core.util.Constants
import com.xpenses.android.core.util.toLongOrZero
import com.xpenses.android.feature_expenses.domain.repository.ExpenseRepository
import com.xpenses.android.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_ADDED
import com.xpenses.android.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_DELETED
import com.xpenses.android.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_UPDATED
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: ExpenseRepository,
    private val preferencesManager: XTPreferencesManager
) : ViewModel(), ExpensesActions {

    private val preferences = preferencesManager.preferences

    private val tags = repo.getTagsList()
    private val selectedTag = savedStateHandle.getLiveData("selectedTag", Constants.STRING_ALL)
    private val tagDeleteModeActive = savedStateHandle.getLiveData("tagDeleteModeActive", false)

    private val months = preferences.flatMapLatest { preferences ->
        repo.getMonthAndExpenditurePercentList(preferences.expenditureLimit)
    }

    private val selectedMonth = savedStateHandle.getLiveData(
        key = "selectedMonth",
        initialValue = TextUtil
            .formatDateWithPattern(System.currentTimeMillis(), Constants.MONTH_YEAR_DB_PATTERN)
    )

    private val showExpenditureLimitUpdateDialog =
        savedStateHandle.getLiveData("showExpenditureLimitUpdateDialog", false)

    private val showTagDeleteConfirmation =
        savedStateHandle.getLiveData("showTagDeleteConfirmation", false)

    private val expenseList = combineTuple(
        selectedMonth.asFlow(),
        selectedTag.asFlow(),
    ).flatMapLatest { (month, tag) ->
        repo.getExpensesListForMonthFilteredByTag(month, tag)
    }

    val state = combineTuple(
        preferences,
        tags,
        selectedTag.asFlow(),
        tagDeleteModeActive.asFlow(),
        months,
        selectedMonth.asFlow(),
        expenseList,
        showExpenditureLimitUpdateDialog.asFlow(),
        showTagDeleteConfirmation.asFlow()
    ).map { (
                preferences,
                tags,
                selectedTag,
                tagDeleteModeActive,
                months,
                selectedMonth,
                expenseList,
                showExpenditureUpdateDialog,
                showTagDeleteConfirmation
            ) ->
        ExpensesState(
            tags = tags,
            selectedTag = selectedTag,
            monthsToExpenditurePercents = months,
            selectedMonth = selectedMonth,
            expenses = expenseList,
            expenditureLimit = preferences.expenditureLimit,
            showExpenditureLimitUpdateDialog = showExpenditureUpdateDialog,
            tagDeletableModeActive = tagDeleteModeActive,
            showTagDeleteConfirmation = showTagDeleteConfirmation
        )
    }.asLiveData()

    private val eventsChannel = Channel<ExpenseListEvent>()
    val events get() = eventsChannel.receiveAsFlow()

    override fun onExpenditureLimitUpdateClick() {
        showExpenditureLimitUpdateDialog.value = true
    }

    override fun onExpenditureLimitUpdateDialogDismiss() {
        showExpenditureLimitUpdateDialog.value = false
    }

    override fun onExpenditureLimitUpdateDialogConfirm(limit: String) {
        viewModelScope.launch {
            val amount = limit.toLongOrZero()
            if (amount <= 0L) {
                eventsChannel.send(ExpenseListEvent.ShowSnackbar(UiText.StringResource(R.string.error_invalid_amount)))
                return@launch
            }
            preferencesManager.updateExpenditureLimit(amount)
            showExpenditureLimitUpdateDialog.value = false
        }
    }

    override fun onTagFilterSelect(tag: String) {
        selectedTag.value = tag
    }

    override fun onTagLongClick() {
        tagDeleteModeActive.value = tagDeleteModeActive.value?.not()
    }

    private var tagDeletion: String? = null
    override fun onTagDeleteClick(tag: String) {
        viewModelScope.launch {
            if (repo.doesExpensesForTagExist(tag)) {
                tagDeletion = tag
                showTagDeleteConfirmation.value = true
            } else {
                deleteTag(tag, false)
            }
        }
    }

    private suspend fun deleteTag(tag: String, hasExpensesLinked: Boolean) {
        if (hasExpensesLinked) {
            repo.deleteTag(tag)
        } else {
            repo.deleteTagWithExpenses(tag)
            tagDeletion = null
            showTagDeleteConfirmation.value = false
        }
        resetSelectedTagIfDeleted(tag)
        tagDeleteModeActive.value = tags.first().isNotEmpty()
    }

    override fun onDeleteExpensesWithTagDismiss() {
        showTagDeleteConfirmation.value = false
    }

    override fun onDeleteExpensesWithTagConfirm() {
        tagDeletion?.let {
            viewModelScope.launch {
                deleteTag(it, true)
            }
        }
    }

    private fun resetSelectedTagIfDeleted(tag: String) {
        if (selectedTag.value == tag) {
            selectedTag.value = Constants.STRING_ALL
        }
    }

    override fun onAddFabClick() {
        viewModelScope.launch {
            eventsChannel.send(ExpenseListEvent.NavigateToAddEditExpenseScreen(AddEditExpenseScreenSpec.buildRoute()))
        }
    }

    override fun onMonthSelect(month: String) {
        selectedMonth.value = month
    }

    override fun onExpenseClick(id: Long) {
        viewModelScope.launch {
            eventsChannel.send(
                ExpenseListEvent.NavigateToAddEditExpenseScreen(
                    AddEditExpenseScreenSpec.buildRoute(id)
                )
            )
        }
    }

    fun onAddEditResult(result: String) {
        val messageRes = when (result) {
            RESULT_EXPENSE_ADDED -> R.string.message_expense_added
            RESULT_EXPENSE_UPDATED -> R.string.message_expense_updated
            RESULT_EXPENSE_DELETED -> R.string.message_expense_deleted
            else -> return
        }
        viewModelScope.launch {
            eventsChannel.send(ExpenseListEvent.ShowSnackbar(UiText.StringResource(messageRes)))
        }
    }

    sealed class ExpenseListEvent {
        data class ShowSnackbar(val message: UiText) : ExpenseListEvent()
        data class NavigateToAddEditExpenseScreen(val route: String) : ExpenseListEvent()
    }
}