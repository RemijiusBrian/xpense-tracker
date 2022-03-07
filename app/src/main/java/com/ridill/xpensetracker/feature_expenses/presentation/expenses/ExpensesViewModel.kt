package com.ridill.xpensetracker.feature_expenses.presentation.expenses

import androidx.annotation.StringRes
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.lifecycle.*
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense
import com.ridill.xpensetracker.feature_expenses.domain.repository.ExpenseRepository
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    cashFlowRepository: CashFlowRepository,
    private val repo: ExpenseRepository,
    private val preferencesManager: XTPreferencesManager
) : ViewModel(), ExpensesActions {

    // Preferences
    private val preferences = preferencesManager.preferences

    // Selected Date
    private val selectedDate =
        savedStateHandle.getLiveData("selectedDate", getCurrentMonth())

    private fun getCurrentMonth(): String =
        SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(System.currentTimeMillis())

    // Months
    private val dateParser = SimpleDateFormat("MM-yyyy", Locale.getDefault())
    private val dateFormatter = SimpleDateFormat("MMMM-yyyy", Locale.getDefault())
    private val datesList = repo.getDates().map { dates ->
        dates.toMutableList().apply {
            val currentMonth = getCurrentMonth()
            if (dates.isEmpty() || dates.first() != currentMonth) {
                add(0, currentMonth)
            }
        }.toList()
    }.map { dates -> dates.map { mapMonthNumberToName(it) } }

    private val dateParserForMonthName = SimpleDateFormat("MMMM-yyyy", Locale.getDefault())
    private val dateFormatterToMonthNumber = SimpleDateFormat("MM-yyyy", Locale.getDefault())

    private fun mapMonthNumberToName(monthNumberString: String): String =
        dateParser.parse(monthNumberString)?.let { dateFormatter.format(it) }.orEmpty()

    private fun mapMonthNameToNumber(monthNameString: String): String =
        dateParserForMonthName.parse(monthNameString)?.let { dateFormatterToMonthNumber.format(it) }
            .orEmpty()

    // Expenses
    private val expenses = selectedDate.asFlow().flatMapLatest { date ->
        repo.getExpensesForMonth(date)
    }

    // Current Expenditure + Balance
    private val currentExpenditure = repo.getExpenditureForCurrentMonth()
    private val balanceAmount = combineTuple(
        preferences,
        currentExpenditure,
        cashFlowRepository.getTotalCashFlowAmount()
    ).map { (preferences, expenditure, cashFlow) ->
        preferences.expenditureLimit -
                (if (preferences.cashFlowIncludedInExpenditure) expenditure + cashFlow else expenditure)
    }
    private val balancePercentage = combineTuple(
        balanceAmount,
        preferences
    ).map { (balance, preferences) -> balance.toFloat() / preferences.expenditureLimit }

    private val showExpenditureLimitUpdateDialog =
        savedStateHandle.getLiveData("showExpenditureLimitUpdateDialog", false)

    // Ui State
    val state = combineTuple(
        datesList,
        expenses,
        preferences,
        currentExpenditure,
        balanceAmount,
        balancePercentage,
        selectedDate.asFlow(),
        showExpenditureLimitUpdateDialog.asFlow()
    ).map { (
                datesList,
                expenses,
                preferences,
                currentExpenditure,
                balance,
                balancePercentage,
                selectedDate,
                showExpenditureLimitUpdateDialog
            ) ->
        ExpensesState(
            datesList = datesList,
            expenses = expenses,
            expenditureLimit = preferences.expenditureLimit.takeIf { it > 0L }
                ?.let { formatAmount(it) }.orEmpty(),
            currentExpenditure = formatAmount(currentExpenditure),
            spendingBalance = formatAmount(balance),
            balancePercentage = balancePercentage,
            selectedDate = mapMonthNumberToName(selectedDate),
            showExpenditureLimitUpdateDialog = showExpenditureLimitUpdateDialog
        )
    }.asLiveData()

    // Events Channel
    private val eventsChannel = Channel<ExpenseEvents>()
    val events: Flow<ExpenseEvents> = eventsChannel.receiveAsFlow()

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
            repo.deleteExpense(expense)
            eventsChannel.send(ExpenseEvents.PerformHapticFeedback(HapticFeedbackType.LongPress))
            eventsChannel.send(ExpenseEvents.ShowUndoDeleteOption(expense))
        }
    }

    fun undoExpenseDelete(expense: Expense) {
        viewModelScope.launch {
            repo.cacheExpense(expense)
        }
    }

    override fun onDateSelect(date: String) {
        val monthFormatted = mapMonthNameToNumber(date)
        if (selectedDate.value == monthFormatted) return
        selectedDate.value = monthFormatted
    }

    override fun onEditExpenditureLimitClick() {
        showExpenditureLimitUpdateDialog.value = true
    }

    override fun onExpenditureLimitUpdateDismiss() {
        showExpenditureLimitUpdateDialog.value = false
    }

    override fun onExpenditureLimitUpdateConfirm(limit: String) {
        viewModelScope.launch {
            val amount = limit.toLongOrNull() ?: preferences.first().expenditureLimit
            if (amount <= 0) {
                showExpenditureLimitUpdateDialog.value = false
                eventsChannel.send(ExpenseEvents.ShowSnackbar(R.string.error_amount_invalid))
                return@launch
            }
            preferencesManager.updateExpenditureLimit(amount)
            showExpenditureLimitUpdateDialog.value = false
            eventsChannel.send(ExpenseEvents.ShowSnackbar(R.string.expenditure_limit_updated))
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