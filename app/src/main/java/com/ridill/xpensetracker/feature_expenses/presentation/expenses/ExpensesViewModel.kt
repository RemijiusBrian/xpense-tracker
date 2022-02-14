package com.ridill.xpensetracker.feature_expenses.presentation.expenses

import androidx.annotation.StringRes
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.lifecycle.*
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.use_case.ExpensesUseCases
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val useCases: ExpensesUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ExpensesActions {

    private val expensePreferences = useCases.getPreferences()

    private val currentlyShownDate =
        savedStateHandle.getLiveData("currentlyShownDate", getCurrentMonth())

    private val monthsList = useCases.getMonthsList()

    private val expenses = currentlyShownDate.asFlow().flatMapLatest { date ->
        useCases.getExpensesForMonth(date)
    }

    // Expenditure Limit + Current Expenditure + Balance
    private val currentExpenditure = useCases.getExpenditureForCurrentMonth()
    private val balanceAmount = useCases.getSpendingBalance()
    private val balancePercentage = combineTuple(
        balanceAmount,
        expensePreferences
    ).map { (balance, preferences) -> balance.toFloat() / preferences.expenditureLimit }

    private val showExpenditureLimitUpdateDialog =
        savedStateHandle.getLiveData("showExpenditureLimitUpdateDialog", false)

    private fun getCurrentMonth(): String =
        SimpleDateFormat("MMMM-yyyy", Locale.getDefault()).format(
            Calendar.getInstance().time
        )

    // Ui State
    val state = combineTuple(
        monthsList,
        expenses,
        expensePreferences,
        currentExpenditure,
        balanceAmount,
        balancePercentage,
        currentlyShownDate.asFlow(),
        showExpenditureLimitUpdateDialog.asFlow()
    ).map { (
                monthsList,
                expenses,
                preferences,
                currentExpenditure,
                balance,
                balancePercentage,
                currentlyShownMonth,
                showExpenditureLimitUpdateDialog
            ) ->
        ExpensesState(
            monthsList = monthsList,
            expenses = expenses,
            expenditureLimit = preferences.expenditureLimit.takeIf { it > 0L }
                ?.let { formatAmount(it) }.orEmpty(),
            currentExpenditure = formatAmount(currentExpenditure),
            spendingBalance = formatAmount(balance),
            balancePercentage = balancePercentage,
            currentlyShownDate = currentlyShownMonth,
            showExpenditureLimitUpdateDialog = showExpenditureLimitUpdateDialog
        )
    }.asLiveData()

    // Events
    private val eventsChannel = Channel<ExpenseEvents>()
    val events = eventsChannel.receiveAsFlow()

    override fun onExpenseClick(expense: Expense) {
        viewModelScope.launch {
            eventsChannel.send(ExpenseEvents.Navigate(Destination.AddEditExpense.buildRoute(expense.id)))
        }
    }

    override fun onAddExpenseClick() {
        viewModelScope.launch {
            eventsChannel.send(ExpenseEvents.Navigate(Destination.AddEditExpense.route))
        }
    }

    override fun onExpenseSwipeDeleted(expense: Expense) {
        viewModelScope.launch {
            useCases.deleteExpense(expense)
            eventsChannel.send(ExpenseEvents.PerformHapticFeedback(HapticFeedbackType.LongPress))
            eventsChannel.send(ExpenseEvents.ShowUndoDeleteOption(expense))
        }
    }

    fun undoExpenseDelete(expense: Expense) {
        viewModelScope.launch {
            useCases.saveExpense(expense)
        }
    }

    override fun onMonthSelect(month: String) {
        if (currentlyShownDate.value == month) return
        currentlyShownDate.value = month
    }

    override fun onEditExpenditureLimitClick() {
        showExpenditureLimitUpdateDialog.value = true
    }

    override fun onExpenditureLimitUpdateDialogDismissed() {
        showExpenditureLimitUpdateDialog.value = false
    }

    override fun onExpenditureLimitUpdateDialogConfirmed(limit: String) {
        viewModelScope.launch {
            when (val result = useCases.updateExpenditureLimit(limit)) {
                is Response.Error -> {
                    eventsChannel.send(
                        ExpenseEvents.ShowSnackbar(result.message ?: R.string.error_unknown)
                    )
                }
                is Response.Success -> {
                    showExpenditureLimitUpdateDialog.value = false
                    eventsChannel.send(ExpenseEvents.ShowSnackbar(R.string.expenditure_limit_updated))
                }
            }
        }
    }

    private fun formatAmount(amount: Long): String =
        "${Currency.getInstance(Locale.getDefault()).symbol} ${TextUtil.formatNumber(amount)}"

    // Events
    sealed class ExpenseEvents {
        data class Navigate(val route: String) : ExpenseEvents()
        data class ShowUndoDeleteOption(val expense: Expense) : ExpenseEvents()
        data class ShowSnackbar(@StringRes val message: Int) : ExpenseEvents()
        data class PerformHapticFeedback(val feedbackType: HapticFeedbackType) : ExpenseEvents()
    }
}