package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_agents.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.components.AddFab
import com.ridill.xpensetracker.core.ui.components.BackArrowButton
import com.ridill.xpensetracker.core.ui.components.EmptyGridIndicator
import com.ridill.xpensetracker.core.ui.components.SearchView
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.theme.PaddingListBottom
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_cash_flow.domain.model.AgentWithAggregate
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_agents.CashFlowActions
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_agents.CashFlowViewModel
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.CASH_FLOW_RESULT
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.RESULT_AGENT_DELETED
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
    val searchModeActive by viewModel.searchModeActive.observeAsState(false)
    val searchQuery by viewModel.searchQuery.observeAsState("")

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
                RESULT_AGENT_DELETED -> R.string.agent_deleted
                else -> return@let
            }
            scaffoldState.snackbarHostState.showSnackbar(context.getString(message))
        }
    }

    ScreenContent(
        scaffoldState = scaffoldState,
        agents = agents,
        actions = viewModel,
        searchModeActive = searchModeActive,
        searchQuery = searchQuery
    )
}

@Composable
private fun ScreenContent(
    scaffoldState: ScaffoldState,
    agents: List<AgentWithAggregate>,
    actions: CashFlowActions,
    searchModeActive: Boolean,
    searchQuery: String
) {
    val gridState = rememberLazyGridState()
    val showAddFab by remember {
        derivedStateOf { gridState.firstVisibleItemIndex < 2 }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            AnimatedVisibility(visible = showAddFab) {
                AddFab(
                    onClick = actions::onAddCashFlowClick,
                    contentDescription = R.string.add_cash_flow
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            TopAppBar(
                title = {
                    AnimatedContent(targetState = searchModeActive) { searchMode ->
                        if (searchMode) {
                            SearchView(
                                query = searchQuery,
                                onQueryChange = actions::onSearchQueryChange,
                                onClearQueryClick = actions::onSearchQueryClear,
                                placeholder = R.string.search,
                            )
                        } else {
                            Text(stringResource(Destination.CashFlow.label))
                        }
                    }
                },
                actions = {
                    AnimatedVisibility(visible = !searchModeActive) {
                        IconButton(onClick = actions::onSearchClick) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = stringResource(R.string.search)
                            )
                        }
                    }
                },
                navigationIcon = if (searchModeActive) {
                    { BackArrowButton(onClick = actions::onSearchDismiss) }
                } else null
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (agents.isEmpty()) {
                    EmptyGridIndicator()
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = PaddingListBottom,
                            start = PaddingMedium,
                            end = PaddingMedium
                        ),
                        state = gridState,
                        columns = GridCells.Fixed(2)
                    ) {
                        items(items = agents, key = { it.id }) { agent ->
                            AgentItem(
                                name = agent.name,
                                isPending = agent.isPending,
                                date = agent.createdDateFormatted,
                                onClick = { actions.onAgentClick(agent.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}