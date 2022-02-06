package com.ridill.xpensetracker.feature_dashboard.presentation.dashboard

import androidx.annotation.StringRes
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.lifecycle.*
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.feature_dashboard.domain.model.Expense
import com.ridill.xpensetracker.feature_dashboard.domain.use_case.DashboardUseCases
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
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
class DashboardViewModel @Inject constructor(
    private val useCases: DashboardUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel(), DashboardActions {

    private val expensePreferences = useCases.getExpensePreference()

    private val expenses = expensePreferences.flatMapLatest { preference ->
        useCases.getExpenses(category = preference.category)
    }

    // Expenditure Limit + Current Expenditure + Balance
    private val currentExpenditure = useCases.getExpenditureForCurrentMonth()
    private val balanceAmount = useCases.getBalance()
    private val balancePercentage = combineTuple(
        balanceAmount,
        expensePreferences
    ).map { (balance, preferences) -> balance.toFloat() / preferences.expenditureLimit }

    private val showExpenditureLimitUpdateDialog =
        savedStateHandle.getLiveData("showExpenditureUpdateDialog", false)

    private val currentlyShownMonth =
        savedStateHandle.getLiveData("currentlyShownMonth", getCurrentMonth())

    private fun getCurrentMonth(): String =
        SimpleDateFormat("MMMM-yyyy", Locale.getDefault()).format(
            Calendar.getInstance().time
        )

    // Ui State
    val state = combineTuple(
        expenses,
        expensePreferences,
        currentExpenditure,
        balanceAmount,
        balancePercentage,
        showExpenditureLimitUpdateDialog.asFlow(),
        currentlyShownMonth.asFlow()
    ).map { (
                expenses,
                preferences,
                currentExpenditure,
                balance,
                balancePercentage,
                showExpenditureLimitUpdateDialog,
                currentlyShownMonth
            ) ->
        DashboardState(
            expenses = expenses,
            selectedExpenseCategory = preferences.category,
            expenditureLimit = formatAmount(preferences.expenditureLimit),
            currentExpenditure = formatAmount(currentExpenditure),
            balance = formatAmount(balance),
            balancePercentage = balancePercentage,
            isBalanceEmpty = preferences.expenditureLimit > 0 && balance <= 0L,
            showExpenditureLimitUpdateDialog = showExpenditureLimitUpdateDialog,
            currentlyShownMonth = currentlyShownMonth
        )
    }.asLiveData()

    // Events
    private val eventsChannel = Channel<ExpenseEvents>()
    val events = eventsChannel.receiveAsFlow()

    override fun onExpenseClick(expense: Expense) {
        viewModelScope.launch {
            val route = when (expense.category) {
                ExpenseCategory.EXPENSE -> Destination.AddEditExpense.buildRoute(expense.id)
                ExpenseCategory.CASH_FLOW -> Destination.CashFlowDetails.buildRoute(expense.id)
                ExpenseCategory.YEARNING -> return@launch
            }
            eventsChannel.send(ExpenseEvents.Navigate(route))
        }
    }

    override fun addExpenseClick(category: ExpenseCategory) {
        viewModelScope.launch {
            val route = when (category) {
                ExpenseCategory.EXPENSE -> Destination.AddEditExpense.route
                ExpenseCategory.CASH_FLOW -> Destination.CashFlowDetails.route
                ExpenseCategory.YEARNING -> return@launch
            }
            eventsChannel.send(ExpenseEvents.Navigate(route))
        }
    }

    override fun onExpenditureLimitUpdate() {
        viewModelScope.launch {
            eventsChannel.send(ExpenseEvents.PerformHapticFeedback(HapticFeedbackType.LongPress))
            showExpenditureLimitUpdateDialog.value = true
        }
    }

    override fun dismissExpenditureLimitUpdateDialog() {
        showExpenditureLimitUpdateDialog.value = false
    }

    override fun updateExpenditureLimit(limit: String) {
        viewModelScope.launch {
            when (val response = useCases.updateExpenditureLimit(limit)) {
                is Response.Error -> {
                    eventsChannel.send(
                        ExpenseEvents.ShowSnackbar(
                            response.message ?: R.string.error_unknown
                        )
                    )
                }
                is Response.Success -> {
                    eventsChannel.send(ExpenseEvents.ShowSnackbar(R.string.expenditure_limit_updated))
                }
            }
            showExpenditureLimitUpdateDialog.value = false
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

    override fun onExpenseCategorySelect(category: ExpenseCategory) {
        viewModelScope.launch {
            useCases.updatePreferenceCategory(category)
        }
    }

    override fun onSettingsClick() {
        viewModelScope.launch {
            eventsChannel.send(ExpenseEvents.Navigate(Destination.Settings.route))
        }
    }

    override fun onMonthClick(month: String) {
        if (currentlyShownMonth.value == month) return
        currentlyShownMonth.value = month
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