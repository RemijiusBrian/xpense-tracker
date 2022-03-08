package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details

import androidx.annotation.StringRes
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.lifecycle.*
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.navigation.NavArgs
import com.ridill.xpensetracker.core.ui.util.TextUtil
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_cash_flow.domain.model.AggregateAmountState
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlow
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowDetailsOptions
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
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
    savedStateHandle: SavedStateHandle,
    private val repo: CashFlowRepository
) : ViewModel(), CashFlowDetailsActions {

    // Agent Live Data
    private val agentLiveData =
        savedStateHandle.getLiveData<CashFlowAgent>(KEY_CASH_FLOW_AGENT)
    val agentName: LiveData<String> = agentLiveData.map { it.name }
    val agentCreatedDate: LiveData<String> = agentLiveData.map { it.createdDateFormatted }

    // Agent Argument
    private val agentIdArg = savedStateHandle.get<Long>(NavArgs.AGENT_ID)
    private var isNew = agentIdArg == -1L

    init {
        if (!savedStateHandle.contains(KEY_CASH_FLOW_AGENT)) {
            if (agentIdArg != null && !isNew) viewModelScope.launch {
                agentLiveData.value = repo.getAgentById(agentIdArg)
                showAddCashFlowButton.value = true
            } else {
                agentLiveData.value = CashFlowAgent.DEFAULT
            }
        }
    }

    // Edit Mode Active
    private val editModeActive = savedStateHandle.getLiveData("editModeActive", isNew)

    // Show Add CashFlow Button
    private val showAddCashFlowButton = savedStateHandle.getLiveData("showAddCashFlowButton", false)

    // Delete agent dialog
    private val showDeleteAgentDialog =
        savedStateHandle.getLiveData("showDeleteAgentDialog", false)

    // Cash Flow cleared dialog
    private val showCashFlowClearedDialog =
        savedStateHandle.getLiveData("showCashFlowClearedDialog", false)

    // CashFlow
    private val cashFlow = agentLiveData.asFlow().flatMapLatest { agent ->
        repo.getCashFlowForAgent(agent.id)
    }

    // Cash Flow Aggregate
    private val cashFlowAggregate = agentLiveData.asFlow().flatMapLatest { agent ->
        repo.getAggregateAmountOfAgent(agent.id)
    }

    private val cashFLowStatus = combineTuple(
        cashFlow,
        cashFlowAggregate,
    ).map { (cashFlow, aggregate) ->
        when {
            cashFlow.isEmpty() -> AggregateAmountState.CLEARED
            aggregate == 0L -> AggregateAmountState.BALANCED
            aggregate > 0 -> AggregateAmountState.IN_CREDIT
            else -> AggregateAmountState.IN_DEBT
        }
    }

    // Active Cash Flow
    private val _activeCashFlow = savedStateHandle.getLiveData<CashFlow?>("activeCashFlow", null)
    val activeCashFlow: LiveData<CashFlow?> = _activeCashFlow

    // Ui State
    val state = combineTuple(
        editModeActive.asFlow(),
        showAddCashFlowButton.asFlow(),
        cashFlow,
        cashFlowAggregate,
        cashFLowStatus,
        showDeleteAgentDialog.asFlow(),
        showCashFlowClearedDialog.asFlow()
    ).map { (
                editMode,
                showAddCashFlowButton,
                cashFlow,
                cashFlowAggregate,
                cashFlowStatus,
                showDeleteAgentDialog,
                showCashFlowClearedDialog
            ) ->
        CashFlowDetailsState(
            editMode = editMode,
            showAddCashFlowButton = showAddCashFlowButton,
            cashFlow = cashFlow,
            aggregateAmount = "${TextUtil.currencySymbol} ${
                TextUtil.formatNumber(abs(cashFlowAggregate))
            }",
            cashFlowStatus = cashFlowStatus,
            showDeleteAgentDialog = showDeleteAgentDialog,
            showCashFlowClearedDialog = showCashFlowClearedDialog
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
                CashFlowDetailsOptions.DELETE_AGENT -> {
                    showDeleteAgentDialog.value = true
                }
            }.exhaustive
        }
    }

    override fun onAgentNameChange(value: String) {
        agentLiveData.value = agentLiveData.value?.copy(name = value)
    }

    override fun onDismissEditMode() {
        agentLiveData.value?.let {
            viewModelScope.launch {
                if (it.id == 0L) {
                    eventsChannel.send(CashFlowDetailsEvents.NavigateBack)
                } else {
                    editModeActive.value = false
                    agentLiveData.value = repo.getAgentById(it.id)
                }
            }
        }
    }

    override fun onSaveAgent() {
        viewModelScope.launch {
            agentLiveData.value?.let {
                if (it.name.isEmpty()) {
                    eventsChannel.send(CashFlowDetailsEvents.ShowSnackbar(R.string.error_name_empty))
                    return@launch
                }
                val trimmedAgent = it.copy(name = it.name.trim())
                val cashFlowAgent = repo.getAgentByName(trimmedAgent.name)
                if (cashFlowAgent != null) {
                    if (isNew) {
                        agentLiveData.value = cashFlowAgent
                        editModeActive.value = false
                        eventsChannel.send(CashFlowDetailsEvents.ShowSnackbar(R.string.agent_already_exists))
                    } else {
                        eventsChannel.send(CashFlowDetailsEvents.ShowSnackbar(R.string.error_name_already_exists))
                    }
                } else {
                    cacheAgent(trimmedAgent)
                    editModeActive.value = false
                    if (isNew) {
                        isNew = false
                        onAddCashFlowClick()
                        eventsChannel.send(CashFlowDetailsEvents.ShowSnackbar(R.string.agent_added))
                    } else {
                        eventsChannel.send(CashFlowDetailsEvents.ShowSnackbar(R.string.agent_updated))
                    }
                }
                showAddCashFlowButton.value = true
            }
        }
    }

    // Cache Agent
    private suspend fun cacheAgent(agent: CashFlowAgent) {
        val insertedId = repo.cacheAgent(agent.copy(name = agent.name.trim()))
        agentLiveData.value = repo.getAgentById(insertedId)
        eventsChannel.send(CashFlowDetailsEvents.ShowSnackbar(R.string.agent_added))
    }

    override fun onAddCashFlowClick() {
        viewModelScope.launch {
            _activeCashFlow.value = CashFlow.default(agentLiveData.value!!.id)
            eventsChannel.send(CashFlowDetailsEvents.ToggleAddEditCashFlow(true))
        }
    }

    override fun onCashFlowNameChange(value: String) {
        _activeCashFlow.value = activeCashFlow.value?.copy(name = value)
    }

    override fun onCashFlowAmountChange(value: String) {
        _activeCashFlow.value =
            activeCashFlow.value?.copy(amount = value.toLongOrNull() ?: 0L)
    }

    override fun onCashFlowLendingChange(value: Boolean) {
        _activeCashFlow.value = activeCashFlow.value?.copy(lending = value)
    }

    override fun onAddEditCashFlowDismiss() {
        viewModelScope.launch {
            _activeCashFlow.value = null
            eventsChannel.send(CashFlowDetailsEvents.ToggleAddEditCashFlow(false))
        }
    }

    override fun onAddEditCashFlowConfirm(repaymentAmount: String) {
        viewModelScope.launch {
            activeCashFlow.value?.let { cashFlow ->
                val repayment = repaymentAmount.toLongOrNull() ?: 0L

                if (validateCashFlow(cashFlow, repayment)) {
                    repo.cacheCashFlow(
                        cashFlow.copy(
                            name = cashFlow.name.trim(),
                            amount = cashFlow.amount - repayment
                        )
                    )
                    eventsChannel.send(
                        CashFlowDetailsEvents.ShowSnackbar(
                            if (cashFlow.id == 0L) R.string.cash_flow_added else R.string.cash_flow_updated
                        )
                    )
                    eventsChannel.send(CashFlowDetailsEvents.ToggleAddEditCashFlow(false))
                }
            }
        }
    }

    private suspend fun validateCashFlow(cashFlow: CashFlow, repayment: Long): Boolean {
        if (cashFlow.name.isEmpty()) {
            eventsChannel.send(CashFlowDetailsEvents.ShowSnackbar(R.string.error_name_empty))
            return false
        }
        if (cashFlow.amount <= 0L) {
            eventsChannel.send(CashFlowDetailsEvents.ShowSnackbar(R.string.error_amount_invalid))
            return false
        }
        if (repayment > cashFlow.amount) {
            eventsChannel.send(CashFlowDetailsEvents.ShowSnackbar(R.string.error_invalid_repayment))
            return false
        }
        return true
    }

    override fun onCashFlowSwipeDelete(cashFlow: CashFlow) {
        viewModelScope.launch {
            repo.deleteCashFlow(cashFlow)
            if (this@CashFlowDetailsViewModel.cashFlow.first().isEmpty()) {
                showCashFlowClearedDialog.value = true
                return@launch
            }
            eventsChannel.send(
                CashFlowDetailsEvents.ProvideHapticFeedback(HapticFeedbackType.LongPress)
            )
            eventsChannel.send(CashFlowDetailsEvents.ShowCashFlowDeleteUndo(cashFlow))
        }
    }

    override fun onUndoCashFlowDelete(cashFlow: CashFlow) {
        viewModelScope.launch {
            repo.cacheCashFlow(cashFlow)
        }
    }

    override fun onCashFlowClick(cashFlow: CashFlow) {
        viewModelScope.launch {
            _activeCashFlow.value = repo.getCashFlowById(cashFlow.id)
            eventsChannel.send(CashFlowDetailsEvents.ToggleAddEditCashFlow(true))
        }
    }

    override fun onDeleteAgentDismiss() {
        showDeleteAgentDialog.value = false
        showCashFlowClearedDialog.value = false
    }

    override fun onDeleteAgentConfirm() {
        viewModelScope.launch {
            agentLiveData.value?.let {
                repo.clearAgentWithCashFlow(it)
                showCashFlowClearedDialog.value = false
                showDeleteAgentDialog.value = false
                eventsChannel.send(
                    CashFlowDetailsEvents.NavigateBackWithResult(
                        RESULT_AGENT_DELETED
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

private const val KEY_CASH_FLOW_AGENT = "KEY_CASH_FLOW_AGENT"

const val CASH_FLOW_RESULT = "CASH_FLOW_RESULT"
const val RESULT_AGENT_DELETED = "RESULT_AGENT_DELETED"