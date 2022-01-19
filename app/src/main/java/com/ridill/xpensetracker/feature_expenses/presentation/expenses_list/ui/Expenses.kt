package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.theme.PaddingListBottom
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.CASH_FLOW_RESULT
import com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.RESULT_CASH_FLOW_CLEARED
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.ADD_EDIT_EXPENSE_RESULT
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_ADDED
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_DELETE
import com.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.RESULT_EXPENSE_UPDATED
import com.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ExpensesActions
import com.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ExpensesState
import com.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ExpensesViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@Composable
fun Expenses(
    navController: NavController
) {
    val viewModel: ExpensesViewModel = hiltViewModel()
    val state by viewModel.state.observeAsState(ExpensesState.initial)
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    val currentBackStackEntry = navController.currentBackStackEntry
    val addEditExpenseResult = currentBackStackEntry
        ?.savedStateHandle?.getLiveData<String>(ADD_EDIT_EXPENSE_RESULT)?.observeAsState()
    val cashFlowResult = currentBackStackEntry?.savedStateHandle
        ?.getLiveData<String>(CASH_FLOW_RESULT)?.observeAsState()

    LaunchedEffect(key1 = addEditExpenseResult) {
        currentBackStackEntry?.savedStateHandle
            ?.remove<String>(ADD_EDIT_EXPENSE_RESULT)

        addEditExpenseResult?.value?.let { result ->
            val message = when (result) {
                RESULT_EXPENSE_ADDED -> R.string.expense_added
                RESULT_EXPENSE_UPDATED -> R.string.expense_updated
                RESULT_EXPENSE_DELETE -> R.string.expense_deleted
                else -> R.string.empty
            }

            context.getString(message).takeIf { it.isNotEmpty() }?.let { msg ->
                scaffoldState.snackbarHostState.showSnackbar(msg)
            }
        }
    }

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

    LaunchedEffect(Unit) {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        viewModel.events.collectLatest { event ->
            when (event) {
                is ExpensesViewModel.ExpenseEvents.Navigate -> {
                    navController.navigate(event.route)
                }
                is ExpensesViewModel.ExpenseEvents.ShowUndoDeleteOption -> {
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
                is ExpensesViewModel.ExpenseEvents.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(event.message)
                    )
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
            state.selectedExpenseCategory?.let {
                AddExpenseFab(
                    currentCategory = it,
                    onClick = { actions.addExpenseClick(it) }
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Destination.Expenses.label)) },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.16f)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExpenditureLimitCard(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        limit = state.expenditureLimit,
                        onClick = actions::onExpenditureLimitCardClick
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ExpenditureOverview(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        expenditure = state.currentExpenditure,
                        balance = state.balance,
                        balancePercent = state.balancePercentage,
                        isBalanceEmpty = state.isBalanceEmpty
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExpenseCategories(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        onCategorySelect = actions::onExpenseCategorySelect,
                        selectedCategory = state.selectedExpenseCategory
                    )
                    ExpenseOptionsMenu(
                        onOptionClick = actions::onMenuOptionClick,
                        showAll = state.showAllExpenses,
                        currentSortCriteria = state.selectedSortCriteria,
                        currentSortOrder = state.selectedSortOrder
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                state.selectedExpenseCategory?.let { category ->
                    AnimatedContent(
                        targetState = category,
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInHorizontally { it } + fadeIn() with
                                        slideOutHorizontally { -it } + fadeOut()
                            } else {
                                slideInHorizontally { -it } + fadeIn() with
                                        slideOutHorizontally { it } + fadeOut()
                            }.using(
                                SizeTransform(clip = false)
                            )
                        }
                    ) { selectedCategory ->
                        val message =
                            if (!state.showAllExpenses && selectedCategory == ExpenseCategory.EXPENSE) {
                                Calendar.getInstance(Locale.getDefault())
                                    .getDisplayName(
                                        Calendar.MONTH,
                                        Calendar.LONG,
                                        Locale.getDefault()
                                    )
                            } else stringResource(category.label)
                        Text(
                            message,
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        bottom = PaddingListBottom
                    )
                ) {
                    items(state.expenses, key = { it.id }) { expense ->
                        if (expense.category == ExpenseCategory.CASH_FLOW) {
                            ExpenseItem(
                                modifier = Modifier
                                    .animateItemPlacement(),
                                name = expense.name,
                                amount = expense.amountFormatted,
                                date = expense.dateFormatted,
                                onClick = { actions.onExpenseClick(expense) },
                                isMonthly = expense.isMonthly,
                                category = expense.category
                            )
                        } else {
                            val dismissState = rememberDismissState(
                                confirmStateChange = {
                                    val isDismissed =
                                        it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart
                                    if (isDismissed) {
                                        actions.onExpenseSwipeDeleted(expense)
                                    }
                                    true
                                }
                            )
                            SwipeToDismiss(
                                state = dismissState,
                                background = {},
                                modifier = Modifier
                                    .animateItemPlacement(),
                            ) {
                                ExpenseItem(
                                    name = expense.name,
                                    amount = expense.amountFormatted,
                                    date = expense.dateFormatted,
                                    onClick = { actions.onExpenseClick(expense) },
                                    isMonthly = expense.isMonthly,
                                    category = expense.category
                                )
                            }
                        }
                    }
                }
            }

            if (state.expenses.isEmpty()) {
                Icon(imageVector = Icons.Default.HourglassEmpty, contentDescription = null)
            }
        }

        if (state.showExpenditureLimitUpdateDialog) {
            ExpenditureUpdateDialog(
                onDismiss = actions::dismissExpenditureLimitUpdateDialog,
                limit = state.expenditureLimit,
                onConfirm = actions::updateExpenditureLimit
            )
        }
    }
}