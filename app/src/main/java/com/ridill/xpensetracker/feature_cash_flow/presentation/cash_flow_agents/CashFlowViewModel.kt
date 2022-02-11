package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_agents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent
import com.ridill.xpensetracker.feature_cash_flow.domain.use_cases.CashFlowUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CashFlowViewModel @Inject constructor(
    useCases: CashFlowUseCases
) : ViewModel(), CashFlowActions {

    // Agents List
    val agents = useCases.getAgents().asLiveData()

    // Events Channel
    private val eventsChannel = Channel<CashFlowEvents>()
    val events = eventsChannel.receiveAsFlow()

    override fun onAgentClick(agent: CashFlowAgent) {
        viewModelScope.launch {
            eventsChannel.send(CashFlowEvents.Navigate(Destination.CashFlowDetails.buildRoute(agent.id)))
        }
    }

    override fun onAddCashFlowClick() {
        viewModelScope.launch {
            eventsChannel.send(CashFlowEvents.Navigate(Destination.CashFlowDetails.route))
        }
    }

    // Events
    sealed class CashFlowEvents {
        data class Navigate(val route: String) : CashFlowEvents()
    }
}