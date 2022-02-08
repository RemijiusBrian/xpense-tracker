package com.ridill.xpensetracker.feature_dashboard.presentation.dashboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.components.EmptyListIndicator
import com.ridill.xpensetracker.core.ui.components.InputDialog
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.theme.PaddingListBottom
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall
import com.ridill.xpensetracker.core.ui.theme.SpacingSmall
import com.ridill.xpensetracker.core.ui.theme.ZeroDp
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.CASH_FLOW_RESULT
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.RESULT_CASH_FLOW_CLEARED
import com.ridill.xpensetracker.feature_dashboard.presentation.dashboard.DashboardActions
import com.ridill.xpensetracker.feature_dashboard.presentation.dashboard.DashboardState
import com.ridill.xpensetracker.feature_dashboard.presentation.dashboard.DashboardViewModel
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.ADD_EDIT_EXPENSE_RESULT
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_ADDED
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_DELETED
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_UPDATED
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Dashboard(
    navController: NavController
) {
    val viewModel: DashboardViewModel = hiltViewModel()
    val state by viewModel.state.observeAsState(DashboardState.INITIAL)
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val hapticFeedback = LocalHapticFeedback.current
    val currentBackStackEntry = navController.currentBackStackEntry

    // Add/Edit Expense Result
    val addEditExpenseResult = currentBackStackEntry
        ?.savedStateHandle?.getLiveData<String>(ADD_EDIT_EXPENSE_RESULT)?.observeAsState()
    LaunchedEffect(key1 = addEditExpenseResult) {
        currentBackStackEntry?.savedStateHandle
            ?.remove<String>(ADD_EDIT_EXPENSE_RESULT)

        addEditExpenseResult?.value?.let { result ->
            val message = when (result) {
                RESULT_EXPENSE_ADDED -> R.string.expense_added
                RESULT_EXPENSE_UPDATED -> R.string.expense_updated
                RESULT_EXPENSE_DELETED -> R.string.expense_deleted
                else -> R.string.empty
            }
            context.getString(message).takeIf { it.isNotEmpty() }?.let { msg ->
                scaffoldState.snackbarHostState.showSnackbar(msg)
            }
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

    // Collect Events
    LaunchedEffect(Unit) {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        viewModel.events.collectLatest { event ->
            when (event) {
                is DashboardViewModel.ExpenseEvents.Navigate -> {
                    navController.navigate(event.route)
                }
                is DashboardViewModel.ExpenseEvents.ShowUndoDeleteOption -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(R.string.expense_deleted),
                        actionLabel = context.getString(R.string.undo)
                    )
                    when (result) {
                        SnackbarResult.Dismissed -> Unit
                        SnackbarResult.ActionPerformed -> {
                            viewModel.undoExpenseDelete(event.expense)
                        }
                    }
                }
                is DashboardViewModel.ExpenseEvents.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(event.message)
                    )
                }
                is DashboardViewModel.ExpenseEvents.PerformHapticFeedback -> {
                    hapticFeedback.performHapticFeedback(event.feedbackType)
                }
            }.exhaustive
        }
    }

    ScreenContent(
        state = state,
        scaffoldState = scaffoldState,
        actions = viewModel,
    )
}

@Composable
private fun ScreenContent(
    state: DashboardState,
    scaffoldState: ScaffoldState,
    actions: DashboardActions,
) {
    val listState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            state.selectedExpenseCategory?.let {
                FloatingActionButton(onClick = { actions.addExpenseFabClick(it) }) {
                    Icon(
                        painter = painterResource(it.icon),
                        contentDescription = stringResource(
                            R.string.add_category_placeholder,
                            stringResource(it.label)
                        )
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Destination.Dashboard.label)) },
                backgroundColor = Color.Transparent,
                elevation = ZeroDp,
                actions = {
                    IconButton(onClick = actions::onSettingsClick) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = PaddingSmall)
        ) {
            Column(
                modifier = Modifier
            ) {
                OverviewCards(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.16f),
                    expenditureLimit = state.expenditureLimit,
                    currentExpenditure = state.currentExpenditure,
                    balance = state.balance,
                    balancePercent = state.balancePercentage,
                    isBalanceEmpty = state.isBalanceEmpty,
                    onEditLimitClick = actions::onEditExpenditureLimitClick
                )
                Spacer(modifier = Modifier.height(SpacingSmall))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExpenseCategories(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = PaddingSmall),
                        onCategorySelect = actions::onExpenseCategorySelect,
                        selectedCategory = state.selectedExpenseCategory
                    )
                }
            }
            Spacer(modifier = Modifier.height(SpacingSmall))
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.expenses.isEmpty()) {
                    EmptyListIndicator()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = PaddingListBottom
                        ),
                        state = listState,
                    ) {
                        state.monthsList.forEach { month ->
                            item(key = month) {
                                DateSeparator(
                                    modifier = Modifier
                                        .animateItemPlacement(),
                                    month = month,
                                    onClick = actions::onMonthClick
                                )
                            }

                            if (state.currentlyShownDate == month) {
                                items(state.expenses, key = { it.id }) { expense ->
                                    ExpenseItem(
                                        modifier = Modifier
                                            .animateItemPlacement(),
                                        name = expense.name,
                                        amount = expense.amountFormatted,
                                        date = expense.dateFormatted,
                                        category = expense.category,
                                        onClick = { actions.onExpenseClick(expense) },
                                        onSwipeDeleted = { actions.onExpenseSwipeDeleted(expense) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Expenditure Limit Update
        if (state.showExpenditureLimitUpdateDialog) {
            InputDialog(
                title = R.string.update_expenditure_limit,
                message = R.string.update_expenditure_limit_message,
                placeholder = state.expenditureLimit.ifEmpty { stringResource(R.string.limit) },
                onDismiss = actions::onExpenditureLimitUpdateDialogDismissed,
                onConfirm = actions::onExpenditureLimitUpdateDialogConfirmed
            )
        }
    }
}

@Composable
private fun DateSeparator(
    modifier: Modifier = Modifier,
    month: String,
    onClick: (String) -> Unit
) {
    TextButton(
        onClick = { onClick(month) },
        modifier = modifier
    ) {
        Text(
            text = month,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold,
        )
    }
}