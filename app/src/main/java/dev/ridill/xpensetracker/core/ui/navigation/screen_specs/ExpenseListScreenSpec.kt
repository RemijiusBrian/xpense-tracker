package dev.ridill.xpensetracker.core.ui.navigation.screen_specs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.ui.components.rememberSnackbarController
import dev.ridill.xpensetracker.core.util.exhaustive
import dev.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.ADD_EDIT_EXPENSE_RESULT
import dev.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ExpenseListScreenContent
import dev.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ExpensesState
import dev.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ExpensesViewModel

object ExpenseListScreenSpec : ScreenSpec {

    override val label: Int = R.string.destination_expenses

    override val navHostRoute: String = "expenseList"

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        val viewModel: ExpensesViewModel = hiltViewModel()
        val state by viewModel.state.observeAsState(ExpensesState.INITIAL)
        val context = LocalContext.current
        val snackbarController = rememberSnackbarController()

        // Add/Edit Expense Result
        val addEditExpenseResult = navBackStackEntry
            .savedStateHandle.getLiveData<String>(ADD_EDIT_EXPENSE_RESULT).observeAsState()
        LaunchedEffect(addEditExpenseResult) {
            navBackStackEntry.savedStateHandle
                .remove<String>(ADD_EDIT_EXPENSE_RESULT)
            addEditExpenseResult.value?.let(viewModel::onAddEditResult)
        }

        // Collect Events
        LaunchedEffect(Unit) {
            viewModel.events.collect { event ->
                when (event) {
                    is ExpensesViewModel.ExpenseListEvent.NavigateToAddEditExpenseScreen -> {
                        navController.navigate(
                            AddEditExpenseScreenSpec.buildRoute(event.id)
                        )
                    }
                    is ExpensesViewModel.ExpenseListEvent.ShowSnackbar -> {
                        snackbarController.showSnackbar(event.message.asString(context))
                    }
                }.exhaustive
            }
        }

        ExpenseListScreenContent(
            state = state,
            snackbarController = snackbarController,
            actions = viewModel,
            navigateToBottomBarDestination = {
                navController.navigate(it.navHostRoute)
            }
        )
    }
}