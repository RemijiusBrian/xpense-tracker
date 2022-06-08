package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.components.SnackbarController
import com.ridill.xpensetracker.core.ui.components.XTSnackbarHost
import com.ridill.xpensetracker.core.ui.components.rememberSnackbarController
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.ui.theme.XpenseTrackerTheme
import com.ridill.xpensetracker.feature_expenses.domain.model.Expense

@Composable
fun ExpensesListScreen(
    navController: NavController
) {
    val viewModel: ExpensesViewModel = hiltViewModel()
//    val state by viewModel.state.observeAsState(ExpensesState.INITIAL)
    val context = LocalContext.current
    val snackbarController = rememberSnackbarController()
    val hapticFeedback = LocalHapticFeedback.current
    val currentBackStackEntry = navController.currentBackStackEntry

    // Add/Edit Expense Result
    /*val addEditExpenseResult = currentBackStackEntry
        ?.savedStateHandle?.getLiveData<String>(ADD_EDIT_EXPENSE_RESULT)?.observeAsState()
    LaunchedEffect(key1 = addEditExpenseResult) {
        currentBackStackEntry?.savedStateHandle
            ?.remove<String>(ADD_EDIT_EXPENSE_RESULT)

        addEditExpenseResult?.value?.let(viewModel::onAddEditResult)
    }*/

    // Collect Events
    LaunchedEffect(Unit) {
        /*viewModel.events.collectLatest { event ->
            @Suppress("IMPLICIT_CAST_TO_ANY")
            when (event) {
                is ExpensesViewModel.ExpenseEvents.Navigate -> {
                    navController.navigate(event.route)
                }
                is ExpensesViewModel.ExpenseEvents.ShowUndoDeleteOption -> {
                    snackbarController.showSnackbarWithAction(
                        message = context.getString(R.string.expense_deleted),
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
                is UiEvent.ShowMessage -> {
                    snackbarController.showSnackbar(event.uiText.asString(context))
                }
                is ExpensesViewModel.ExpenseEvents.PerformHapticFeedback -> {
                    hapticFeedback.performHapticFeedback(event.feedbackType)
                }
                else -> Unit
            }.exhaustive
        }*/
    }

    ScreenContent(
        state = ExpensesState.INITIAL,
        snackbarController = snackbarController,
        actions = viewModel
    )
}

@Composable
private fun ScreenContent(
    state: ExpensesState,
    snackbarController: SnackbarController,
    actions: ExpensesActions,
) {
    Scaffold(
        snackbarHost = { XTSnackbarHost(snackbarController) },
        bottomBar = {
            BottomAppBar(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = actions::onAddExpenseClick,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = stringResource(R.string.content_description_add_expense)
                        )
                    }
                },
                icons = {
                    repeat(3) {
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

            }
            LazyColumn(
                contentPadding = paddingValues
            ) {
                items(state.expenses, key = { it.id }) { expense ->
                    ExpenseItem(name = expense.name, onClick = {})
                }
            }
        }
    }
}

@Composable
private fun ExpenseItem(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(PaddingMedium),
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier
                .padding(PaddingMedium)
        ) {
        }
    }
}

@Preview
@Composable
private fun PreviewScreenContent() {
    XpenseTrackerTheme {
        ScreenContent(
            state = ExpensesState.INITIAL,
            snackbarController = rememberSnackbarController(),
            actions = object : ExpensesActions {
                override fun onExpenseClick(expense: Expense) {}
                override fun onAddExpenseClick() {}
                override fun onExpenseSwipeDeleted(expense: Expense) {}
                override fun onDateSelect(date: String) {}
                override fun onEditExpenditureLimitClick() {}
                override fun onExpenditureLimitUpdateDismiss() {}
                override fun onExpenditureLimitUpdateConfirm(limit: String) {}
            }
        )
    }
}