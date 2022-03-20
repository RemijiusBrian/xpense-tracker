package com.ridill.xpensetracker.feature_expenses.presentation.expenses.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.components.AddFab
import com.ridill.xpensetracker.core.ui.components.EmptyListIndicator
import com.ridill.xpensetracker.core.ui.components.InputDialog
import com.ridill.xpensetracker.core.ui.components.TransparentTopAppBar
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.theme.PaddingListBottom
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall
import com.ridill.xpensetracker.core.ui.theme.SpacingSmall
import com.ridill.xpensetracker.core.ui.util.rememberSnackbarController
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.ADD_EDIT_EXPENSE_RESULT
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_ADDED
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_DELETED
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_UPDATED
import com.ridill.xpensetracker.feature_expenses.presentation.expenses.ExpensesActions
import com.ridill.xpensetracker.feature_expenses.presentation.expenses.ExpensesState
import com.ridill.xpensetracker.feature_expenses.presentation.expenses.ExpensesViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Expenses(
    navController: NavController
) {
    val viewModel: ExpensesViewModel = hiltViewModel()
    val state by viewModel.state.observeAsState(ExpensesState.INITIAL)
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val hapticFeedback = LocalHapticFeedback.current
    val currentBackStackEntry = navController.currentBackStackEntry
    val coroutineScope = rememberCoroutineScope()
    val snackbarController = rememberSnackbarController(coroutineScope)

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
                else -> return@let
            }
            scaffoldState.snackbarHostState.showSnackbar(context.getString(message))
        }
    }

    // Collect Events
    LaunchedEffect(Unit) {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        viewModel.events.collectLatest { event ->
            when (event) {
                is ExpensesViewModel.ExpenseEvents.Navigate -> {
                    navController.navigate(event.route)
                }
                is ExpensesViewModel.ExpenseEvents.ShowUndoDeleteOption -> {
                    snackbarController.showSnackbar(
                        scaffoldState,
                        context.getString(R.string.expense_deleted),
                        actionLabel = context.getString(R.string.undo),
                        onActionPerformed = { result ->
                            when (result) {
                                SnackbarResult.Dismissed -> Unit
                                SnackbarResult.ActionPerformed -> {
                                    viewModel.undoExpenseDelete(event.expense)
                                }
                            }
                        }
                    )
                }
                is ExpensesViewModel.ExpenseEvents.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(event.message)
                    )
                }
                is ExpensesViewModel.ExpenseEvents.PerformHapticFeedback -> {
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
    state: ExpensesState,
    scaffoldState: ScaffoldState,
    actions: ExpensesActions,
) {
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            AddFab(
                onClick = actions::onAddExpenseClick,
                contentDescription = R.string.add_expense
            )
        },
        topBar = { TransparentTopAppBar(stringResource(Destination.Expenses.label)) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = PaddingSmall)
        ) {
            OverviewCards(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.20f),
                expenditureLimit = state.expenditureLimit,
                currentExpenditure = state.currentExpenditure,
                spendingBalance = state.spendingBalance,
                balancePercent = state.balancePercentage,
                onEditLimitClick = actions::onEditExpenditureLimitClick
            )
            Spacer(modifier = Modifier.height(SpacingSmall))
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(bottom = PaddingListBottom),
                ) {
                    state.datesList.forEach { date ->
                        item(key = date) {
                            ExpenseDateSeparator(
                                modifier = Modifier
                                    .animateItemPlacement(),
                                date = date,
                                onClick = actions::onDateSelect
                            )
                        }

                        if (state.selectedDate == date) {
                            if (state.expenses.isEmpty()) {
                                item {
                                    EmptyListIndicator(
                                        modifier = Modifier
                                            .fillParentMaxWidth(),
                                        message = R.string.empty_expenses
                                    )
                                }
                            } else {
                                items(state.expenses, key = { it.id }) { expense ->
                                    ExpenseItem(
                                        modifier = Modifier
                                            .animateItemPlacement(),
                                        name = expense.name,
                                        amount = expense.amountFormatted,
                                        date = expense.dateFormatted,
                                        isMonthly = expense.isMonthly,
                                        onClick = { actions.onExpenseClick(expense) },
                                        onSwipeDeleted = { actions.onExpenseSwipeDeleted(expense) },
                                        tag = expense.tag
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
                onDismiss = actions::onExpenditureLimitUpdateDismiss,
                onConfirm = actions::onExpenditureLimitUpdateConfirm
            )
        }
    }
}