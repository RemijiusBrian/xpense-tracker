package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_agents.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.components.AddFab
import com.ridill.xpensetracker.core.ui.components.EmptyGridIndicator
import com.ridill.xpensetracker.core.ui.components.TransparentTopAppBar
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.theme.PaddingListBottom
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_cash_flow.domain.model.CashFlowAgent
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_agents.CashFlowActions
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_agents.CashFlowViewModel
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.CASH_FLOW_RESULT
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.RESULT_CASH_FLOW_CLEARED
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CashFlow(
    navController: NavController
) {
    val viewModel: CashFlowViewModel = hiltViewModel()
    val agents by viewModel.agents.observeAsState(emptyList())
    val currentBackStackEntry = navController.currentBackStackEntry
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    // Collect Events
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is CashFlowViewModel.CashFlowEvents.Navigate -> {
                    navController.navigate(event.route)
                }
            }.exhaustive
        }
    }

    // CashFlow Result
    val cashFlowResult = currentBackStackEntry?.savedStateHandle
        ?.getLiveData<String>(CASH_FLOW_RESULT)?.observeAsState()
    LaunchedEffect(key1 = cashFlowResult) {
        currentBackStackEntry?.savedStateHandle
            ?.remove<String>(CASH_FLOW_RESULT)

        cashFlowResult?.value?.let { result ->
            val message = when (result) {
                RESULT_CASH_FLOW_CLEARED -> R.string.cash_flow_cleared
                else -> R.string.empty
            }
            context.getString(message).takeIf { it.isNotEmpty() }?.let { msg ->
                scaffoldState.snackbarHostState.showSnackbar(msg)
            }
        }
    }

    ScreenContent(
        scaffoldState = scaffoldState,
        agents = agents,
        actions = viewModel
    )
}

@Composable
private fun ScreenContent(
    scaffoldState: ScaffoldState,
    agents: List<CashFlowAgent>,
    actions: CashFlowActions
) {
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            AddFab(
                onClick = actions::onAddCashFlowClick,
                contentDescription = R.string.add_cash_flow
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            TransparentTopAppBar(
                title = stringResource(Destination.CashFlow.label)
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (agents.isEmpty()) {
                EmptyGridIndicator()
            } else {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        bottom = PaddingListBottom,
                        start = PaddingMedium,
                        end = PaddingMedium
                    )
                ) {
                    items(agents) { agent ->
                        AgentItem(
                            name = agent.name,
                            pending = agent.isPending,
                            onClick = { actions.onAgentClick(agent) }
                        )
                    }
                }
            }
        }
    }
}