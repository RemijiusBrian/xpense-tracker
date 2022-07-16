package dev.ridill.xpensetracker.feature_expenses.presentation.expenses_list

import androidx.lifecycle.*
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import dev.ridill.xpensetracker.core.domain.model.UiText
import dev.ridill.xpensetracker.core.ui.util.TextUtil
import dev.ridill.xpensetracker.core.util.Constants
import dev.ridill.xpensetracker.core.util.DatePatterns
import dev.ridill.xpensetracker.core.util.orZero
import dev.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import dev.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_ADDED
import dev.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_DELETED
import dev.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_UPDATED
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: ExpenseRepository,
    preferencesManager: XTPreferencesManager
) : ViewModel(), ExpensesActions {

    private val preferences = preferencesManager.preferences

    private val tags = repo.getTagsList()
    private val selectedTag = savedStateHandle.getLiveData("selectedTag", "")
    private val tagDeleteModeActive = savedStateHandle.getLiveData("tagDeleteModeActive", false)

    private val selectedMonth = savedStateHandle.getLiveData(
        key = "selectedMonth",
        initialValue = getCurrentMonthWithYear()
    )
    private val months = preferences.flatMapLatest { preferences ->
        repo.getMonthAndExpenditurePercentList(preferences.expenditureLimit)
    }.onEach {
        selectedMonth.value = selectedMonth.value
            ?: it.firstOrNull()?.month
                    ?: getCurrentMonthWithYear()
    }

    private fun getCurrentMonthWithYear(): String = TextUtil.formatDate(
        System.currentTimeMillis(),
        DatePatterns.MONTH_NUMBER_WITH_YEAR
    )

    private val showTagDeleteConfirmation =
        savedStateHandle.getLiveData("showTagDeleteConfirmation", false)

    private val expenseList = combineTuple(
        selectedMonth.asFlow(),
        selectedTag.asFlow(),
    ).flatMapLatest { (month, tag) ->
        repo.getExpensesListForMonthFilteredByTag(month, tag)
    }

    private val balanceForCurrentMonth = combineTuple(
        repo.getExpenditureForCurrentMonth(),
        preferences
    ).map { (expenditure, preference) ->
        preference.expenditureLimit - expenditure
    }

    private val balancePercentageForCurrentMonth = combineTuple(
        balanceForCurrentMonth,
        preferences
    ).map { (balance, preference) ->
        (balance / preference.expenditureLimit).toFloat().takeIf { !it.isNaN() }.orZero()
    }.map { it.coerceIn(0f..1f) }

    val state = combineTuple(
        tags,
        selectedTag.asFlow(),
        tagDeleteModeActive.asFlow(),
        months,
        selectedMonth.asFlow(),
        expenseList,
        showTagDeleteConfirmation.asFlow(),
        balanceForCurrentMonth,
        balancePercentageForCurrentMonth,
        preferences
    ).map { (
                tags,
                selectedTag,
                tagDeleteModeActive,
                months,
                selectedMonth,
                expenseList,
                showTagDeleteConfirmation,
                balanceForCurrentMonth,
                balancePercentageForCurrentMonth,
                preferences
            ) ->
        ExpensesState(
            tags = tags,
            selectedTag = selectedTag,
            monthsToExpenditurePercents = months,
            selectedMonth = selectedMonth,
            expenses = expenseList,
            tagDeletionModeActive = tagDeleteModeActive,
            showTagDeleteConfirmation = showTagDeleteConfirmation,
            balance = balanceForCurrentMonth,
            balancePercent = balancePercentageForCurrentMonth,
            isLimitSet = preferences.isExpenditureLimitSet,
            showLowBalanceWarning = preferences.balanceWarningEnabled &&
                    balancePercentageForCurrentMonth <= preferences.balanceWarningPercent
        )
    }.asLiveData()

    private val eventsChannel = Channel<ExpenseListEvent>()
    val events get() = eventsChannel.receiveAsFlow()

    override fun onTagFilterSelect(tag: String) {
        if (tagDeleteModeActive.value == true) {
            tagDeleteModeActive.value = false
        } else {
            selectedTag.value = tag.takeIf { it != Constants.STRING_ALL }.orEmpty()
        }
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
            eventsChannel.send(
                ExpenseListEvent.NavigateToAddEditExpenseScreen(-1L)
            )
        }
    }

    override fun onMonthSelect(month: String) {
        selectedMonth.value = month
    }

    override fun onExpenseClick(id: Long) {
        viewModelScope.launch {
            eventsChannel.send(
                ExpenseListEvent.NavigateToAddEditExpenseScreen(id)
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
            eventsChannel.send(ExpenseListEvent.ShowUiMessage(UiText.StringResource(messageRes)))
        }
    }

    sealed class ExpenseListEvent {
        data class ShowUiMessage(val message: UiText) : ExpenseListEvent()
        data class ShowErrorMessage(val message: UiText) : ExpenseListEvent()
        data class NavigateToAddEditExpenseScreen(val id: Long) : ExpenseListEvent()
    }
}