package com.ridill.xpensetracker.feature_dashboard.presentation.dashboard

import androidx.annotation.StringRes
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.lifecycle.*
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.feature_dashboard.domain.use_case.DashboardUseCases
import com.ridill.xpensetracker.feature_expenditures.domain.model.Expenditure
import com.ridill.xpensetracker.feature_expenditures.domain.model.ExpenditureCategory
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

    private val preferences = useCases.getDashboardPreference()

    private val expenditures = preferences.flatMapLatest { preference ->
        useCases.getExpenditures(category = preference.category)
    }

    // Expenditure Limit + Current Expenditure + Balance
    private val currentExpenditure = useCases.getExpenditureForCurrentMonth()
    private val balanceAmount = useCases.getBalance()
    private val balancePercentage = combineTuple(
        balanceAmount,
        preferences
    ).map { (balance, preferences) -> balance.toFloat() / preferences.expenditureLimit }

    private val showExpenditureLimitUpdateDialog =
        savedStateHandle.getLiveData("showExpenditureUpdateDialog", false)

    private val currentlyShownMonth =
        savedStateHandle.getLiveData("currentlyShownMonth", getCurrentMonth())

    private fun getCurrentMonth(): String =
        SimpleDateFormat("MMMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().time)

    // Ui State
    val state = combineTuple(
        expenditures,
        preferences,
        currentExpenditure,
        balanceAmount,
        balancePercentage,
        showExpenditureLimitUpdateDialog.asFlow(),
        currentlyShownMonth.asFlow()
    ).map { (
                expenditures,
                preferences,
                currentExpenditure,
                balance,
                balancePercentage,
                showExpenditureLimitUpdateDialog,
                currentlyShownMonth
            ) ->
        DashboardState(
            expenditures = expenditures,
            selectedExpenditureCategory = preferences.category,
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

    override fun onExpenditureClick(expenditure: Expenditure) {
        viewModelScope.launch {
            val route = when (expenditure.category) {
                ExpenditureCategory.EXPENDITURE -> Destination.AddEditExpense.buildRoute(expenditure.id)
                ExpenditureCategory.CASH_FLOW -> Destination.CashFlowDetails.buildRoute(expenditure.id)
                ExpenditureCategory.YEARNING -> return@launch
            }
            eventsChannel.send(ExpenseEvents.Navigate(route))
        }
    }

    override fun addExpenditureClick(category: ExpenditureCategory) {
        viewModelScope.launch {
            val route = when (category) {
                ExpenditureCategory.EXPENDITURE -> Destination.AddEditExpense.route
                ExpenditureCategory.CASH_FLOW -> Destination.CashFlowDetails.route
                ExpenditureCategory.YEARNING -> return@launch
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

    override fun onExpenditureLimitUpdateDismiss() {
        showExpenditureLimitUpdateDialog.value = false
    }

    override fun onExpenditureLimitUpdateConfirm(limit: String) {
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

    override fun onExpenditureSwipeDeleted(expenditure: Expenditure) {
        viewModelScope.launch {
            useCases.deleteExpenditure(expenditure)
            eventsChannel.send(ExpenseEvents.PerformHapticFeedback(HapticFeedbackType.LongPress))
            eventsChannel.send(ExpenseEvents.ShowUndoDeleteOption(expenditure))
        }
    }

    fun undoExpenseDelete(expenditure: Expenditure) {
        viewModelScope.launch {
            useCases.saveExpenditure(expenditure)
        }
    }

    override fun onExpenditureCategorySelect(category: ExpenditureCategory) {
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
        data class ShowUndoDeleteOption(val expenditure: Expenditure) : ExpenseEvents()
        data class ShowSnackbar(@StringRes val message: Int) : ExpenseEvents()
        data class PerformHapticFeedback(val feedbackType: HapticFeedbackType) : ExpenseEvents()
    }
}