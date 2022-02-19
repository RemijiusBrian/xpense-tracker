package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_agents

import androidx.lifecycle.*
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.feature_cash_flow.domain.repository.CashFlowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CashFlowViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: CashFlowRepository
) : ViewModel(), CashFlowActions {

    // Events Channel
    private val eventsChannel = Channel<CashFlowEvents>()
    val events = eventsChannel.receiveAsFlow()

    // Search Mode
    private val _searchModeActive = savedStateHandle.getLiveData("searchModeActive", false)
    val searchModeActive: LiveData<Boolean> = _searchModeActive

    // Search Query
    private val _searchQuery = savedStateHandle.getLiveData("searchQuery", "")
    val searchQuery: LiveData<String> = _searchQuery

    // Agents List
    val agents = searchQuery.asFlow().flatMapLatest { query ->
        repo.getAgents(query)
    }.asLiveData()

    override fun onAgentClick(agentId: Long) {
        viewModelScope.launch {
            eventsChannel.send(
                CashFlowEvents.Navigate(
                    Destination.CashFlowDetails.buildRoute(agentId)
                )
            )
        }
    }

    override fun onAddCashFlowClick() {
        viewModelScope.launch {
            eventsChannel.send(CashFlowEvents.Navigate(Destination.CashFlowDetails.route))
        }
    }

    override fun onSearchClick() {
        _searchModeActive.value = true
    }

    override fun onSearchDismiss() {
        _searchQuery.value = ""
        _searchModeActive.value = false
    }

    override fun onSearchQueryClear() {
        _searchQuery.value = ""
    }

    override fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    // Events
    sealed class CashFlowEvents {
        data class Navigate(val route: String) : CashFlowEvents()
    }
}