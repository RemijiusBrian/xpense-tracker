package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details

import androidx.annotation.StringRes
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.lifecycle.*
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.navigation.NavArgs
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.core.util.Response
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowDetailsOptions
import com.ridill.xpensetracker.feature_cash_flow.domain.use_cases.CashFlowDetailsUseCases
import com.ridill.xpensetracker.feature_dashboard.domain.model.Expense
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class CashFlowDetailsViewModel @Inject constructor(
    private val useCases: CashFlowDetailsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel(), CashFlowDetailsActions {

    // Expense Argument
    private val expenseArg = savedStateHandle.get<Long>(NavArgs.CASH_FLOW_EXPENSE)
    private var isNew = expenseArg == -1L

    // Expense
    private val expense =
        savedStateHandle.getLiveData(KEY_CASH_FLOW_EXPENSE, Expense.CASH_FLOW_DEFAULT)
    val expenseName: LiveData<String> = expense.map { it.name }

    init {
        if (!savedStateHandle.contains(KEY_CASH_FLOW_EXPENSE)) {
            if (expenseArg != null && !isNew) viewModelScope.launch {
                expense.value = useCases.getExpenseById(expenseArg)
                showAddCashFlowButton.value = true
            } else {
                expense.value = Expense.CASH_FLOW_DEFAULT
            }
        }
    }

    // Edit Mode Active
    private val editModeActive = savedStateHandle.getLiveData("editModeActive", isNew)

    // Show Add CashFlow Button
    private val showAddCashFlowButton = savedStateHandle.getLiveData("showAddCashFlowButton", false)

    // CashFlow
    private val cashFlow = expense.asFlow().flatMapLatest {
        useCases.getCashFlow(it.id)
    }

    // Overall Amount
    private val aggregateAmount = expense.asFlow().flatMapLatest {
        useCases.getOverallAmount(it.id)
    }
    private val cashFLowStatus = combineTuple(
        cashFlow,
        aggregateAmount
    ).map { (cashFlow, aggregate) ->
        useCases.mapToCashFlowStatus(cashFlow, aggregate)
    }

    // Active Cash Flow
    private val _activeCashFlow = savedStateHandle.getLiveData<CashFlow?>("activeCashFlow", null)
    val activeCashFlow: LiveData<CashFlow?> = _activeCashFlow

    // Show Clear Confirmation Dialog
    private val showStrikeOffConfirmationDialog =
        savedStateHandle.getLiveData("showClearConfirmationDialog", false)

    // Ui State
    val state = combineTuple(
        editModeActive.asFlow(),
        showAddCashFlowButton.asFlow(),
        cashFlow,
        aggregateAmount,
        cashFLowStatus,
        showStrikeOffConfirmationDialog.asFlow()
    ).map { (
                editMode,
                showAddCashFlowButton,
                cashFlow,
                overallAmount,
                cashFlowStatus,
                showClearConfirmationDialog
            ) ->
        CashFlowDetailsState(
            editMode = editMode,
            showAddCashFlowButton = showAddCashFlowButton,
            cashFlow = cashFlow,
            aggregateAmount = "${TextUtil.currencySymbol} ${
                TextUtil.formatNumber(abs(overallAmount))
            }",
            cashFlowStatus = cashFlowStatus,
            showClearCashFlowConfirmation = showClearConfirmationDialog
        )
    }.asLiveData()

    // Events Channel
    private val eventsChannel = Channel<CashFlowDetailsEvents>()
    val events = eventsChannel.receiveAsFlow()

    override fun onMenuOptionSelect(option: CashFlowDetailsOptions) {
        viewModelScope.launch {
            when (option) {
                CashFlowDetailsOptions.EDIT -> {
                    editModeActive.value = !editModeActive.value!!
                }
                CashFlowDetailsOptions.STRIKE_OFF -> {
                    showStrikeOffConfirmationDialog.value = true
                }
            }.exhaustive
        }
    }

    override fun onExpenseNameChange(value: String) {
        expense.value = expense.value?.copy(name = value)
    }

    override fun onDismissEditMode() {
        expense.value?.let {
            if (it.id == 0L) viewModelScope.launch {
                eventsChannel.send(CashFlowDetailsEvents.NavigateBack)
            } else {
                editModeActive.value = false
            }
        }
    }

    override fun onConfirmExpenseNameChange() {
        viewModelScope.launch {
            expense.value?.let {
                if (isNew) {
                    if (useCases.doesExpenseAlreadyExist(it.name)) {
                        expense.value = useCases.getExpenseByName(it.name)
                        editModeActive.value = false
                        showAddCashFlowButton.value = true
                        eventsChannel.send(CashFlowDetailsEvents.ShowSnackbar(R.string.error_name_already_exists))
                        return@launch
                    }
                }
                when (val response = useCases.saveExpense(it)) {
                    is Response.Error -> {
                        eventsChannel.send(
                            CashFlowDetailsEvents.ShowSnackbar(
                                response.message ?: R.string.error_unknown
                            )
                        )
                    }
                    is Response.Success -> {
                        response.data?.let { insertedId ->
                            expense.value = useCases.getExpenseById(insertedId)
                            eventsChannel.send(
                                CashFlowDetailsEvents.ProvideHapticFeedback(
                                    HapticFeedbackType.LongPress
                                )
                            )
                            editModeActive.value = false
                            showAddCashFlowButton.value = true
                            if (isNew) {
                                onAddCashFlowClick()
                                isNew = false
                            } else {
                                eventsChannel.send(CashFlowDetailsEvents.ShowSnackbar(R.string.name_updated))
                            }
                        }
                    }
                }
            }
        }
    }

    // Update Expense
    private suspend fun updateExpense(updatedExpense: Expense) {
        when (val response = useCases.saveExpense(updatedExpense)) {
            is Response.Error -> {
                eventsChannel.send(
                    CashFlowDetailsEvents.ShowSnackbar(
                        response.message ?: R.string.error_unknown
                    )
                )
            }
            is Response.Success ->
                response.data?.let { expense.value = useCases.getExpenseById(it) }
        }
    }

    override fun onAddCashFlowClick() {
        viewModelScope.launch {
            _activeCashFlow.value = CashFlow.default(expense.value!!.id)
            eventsChannel.send(CashFlowDetailsEvents.ToggleAddEditCashFlow(true))
        }
    }

    override fun onDismissAddEditCashFlow() {
        viewModelScope.launch {
            _activeCashFlow.value = null
            eventsChannel.send(CashFlowDetailsEvents.ToggleAddEditCashFlow(false))
        }
    }

    override fun onCashFlowNameChange(value: String) {
        _activeCashFlow.value = activeCashFlow.value?.copy(name = value)
    }

    override fun onCashFlowAmountChange(value: String) {
        _activeCashFlow.value = activeCashFlow.value?.copy(amount = value.toLongOrNull() ?: 0L)
    }

    override fun onCashFlowLendingChange(value: Boolean) {
        _activeCashFlow.value = activeCashFlow.value?.copy(lending = value)
    }

    private suspend fun addCashFlow(cashFlow: CashFlow, repaymentAmount: String = "") {
        when (val response = useCases.saveCashFlow(cashFlow, repaymentAmount)) {
            is Response.Error -> {
                eventsChannel.send(
                    CashFlowDetailsEvents.ShowSnackbar(
                        response.message
                            ?: R.string.error_unknown
                    )
                )
            }
            is Response.Success -> {
                updateExpense(expense.value!!.copy(amount = aggregateAmount.first()))
                _activeCashFlow.value = null
                eventsChannel.send(
                    CashFlowDetailsEvents.ProvideHapticFeedback(
                        HapticFeedbackType.LongPress
                    )
                )
                eventsChannel.send(CashFlowDetailsEvents.ToggleAddEditCashFlow(false))
            }
        }
    }

    private suspend fun deleteCashFlow(cashFlow: CashFlow) {
        useCases.deleteCashFlow(cashFlow)
        updateExpense(expense.value!!.copy(amount = aggregateAmount.first()))
    }

    override fun onAddEditCashFlowConfirm(repaymentAmount: String) {
        viewModelScope.launch {
            activeCashFlow.value?.let { cashFlow ->
                addCashFlow(cashFlow, repaymentAmount = repaymentAmount)
            }
        }
    }

    override fun onCashFlowSwipeDelete(cashFlow: CashFlow) {
        viewModelScope.launch {
            deleteCashFlow(cashFlow)
            eventsChannel.send(
                CashFlowDetailsEvents.ProvideHapticFeedback(
                    HapticFeedbackType.LongPress
                )
            )
            eventsChannel.send(CashFlowDetailsEvents.ShowCashFlowDeleteUndo(cashFlow))
        }
    }

    override fun onUndoCashFlowDelete(cashFlow: CashFlow) {
        viewModelScope.launch {
            useCases.saveCashFlow(cashFlow, repayment = "")
            updateExpense(expense.value!!.copy(amount = aggregateAmount.first()))
        }
    }

    override fun onCashFlowClick(cashFlow: CashFlow) {
        viewModelScope.launch {
            _activeCashFlow.value = useCases.getCashFlowById(cashFlow.expense, cashFlow.name)
            eventsChannel.send(CashFlowDetailsEvents.ToggleAddEditCashFlow(true))
        }
    }

    override fun onStrikeOffDismiss() {
        showStrikeOffConfirmationDialog.value = false
    }

    override fun onStrikeOffConfirm() {
        viewModelScope.launch {
            expense.value?.let {
                showStrikeOffConfirmationDialog.value = false
                useCases.strikeOffCashFlow(it)
                eventsChannel.send(
                    CashFlowDetailsEvents.NavigateBackWithResult(
                        RESULT_CASH_FLOW_CLEARED
                    )
                )
            }
        }
    }

    // Events
    sealed class CashFlowDetailsEvents {
        data class ShowSnackbar(@StringRes val message: Int) : CashFlowDetailsEvents()
        data class ToggleAddEditCashFlow(val show: Boolean) : CashFlowDetailsEvents()
        data class ShowCashFlowDeleteUndo(val cashFlow: CashFlow) : CashFlowDetailsEvents()
        data class NavigateBackWithResult(val result: String) : CashFlowDetailsEvents()
        object NavigateBack : CashFlowDetailsEvents()
        data class ProvideHapticFeedback(val feedbackType: HapticFeedbackType) :
            CashFlowDetailsEvents()
    }
}


private const val KEY_CASH_FLOW_EXPENSE = "KEY_CASH_FLOW_EXPENSE"

const val CASH_FLOW_RESULT = "CASH_FLOW_RESULT"
const val RESULT_CASH_FLOW_CLEARED = "RESULT_CASH_FLOW_CLEARED"