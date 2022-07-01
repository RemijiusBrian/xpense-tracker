package dev.ridill.xpensetracker.core.ui.navigation.screen_specs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.*
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.ui.components.rememberSnackbarController
import dev.ridill.xpensetracker.core.util.exhaustive
import dev.ridill.xpensetracker.feature_expenses.presentation.add_edit_expense.*

object AddEditExpenseScreenSpec : ScreenSpec {

    override val label: Int = R.string.destination_add_edit_expense

    override val navHostRoute: String = "add_edit_expense?$ARG_EXPENSE_ID={$ARG_EXPENSE_ID}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(ARG_EXPENSE_ID) {
                type = NavType.LongType
                nullable = false
                defaultValue = NO_EXPENSE_ID
            }
        )

    override val deepLinks: List<NavDeepLink>
        get() = listOf(
            navDeepLink {
                uriPattern =
                    "https://www.xpensetracker.ridill.dev/add_edit_expense/{$ARG_EXPENSE_ID}"
            }
        )

    fun isNewExpenseMode(expenseId: Long?): Boolean =
        expenseId != null && expenseId != NO_EXPENSE_ID

    fun buildRoute(expenseId: Long = NO_EXPENSE_ID): String =
        "add_edit_expense?$ARG_EXPENSE_ID=$expenseId"

    fun getExpenseIdFromSavedStateHandle(savedStateHandle: SavedStateHandle): Long? =
        savedStateHandle.get<Long>(ARG_EXPENSE_ID)

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        val viewModel: AddEditExpenseViewModel = hiltViewModel()
        val amount by viewModel.amount.observeAsState("")
        val name by viewModel.name.observeAsState("")
        val state by viewModel.state.observeAsState(AddEditExpenseState.INITIAL)
        val newTagInput by viewModel.newTagInput.observeAsState("")

        val snackbarController = rememberSnackbarController()
        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(snackbarController, context) {
            @Suppress("IMPLICIT_CAST_TO_ANY")
            viewModel.events.collect { event ->
                when (event) {
                    AddEditExpenseViewModel.AddEditEvents.ExpenseCreated -> {
                        keyboardController?.hide()
                        navController.previousBackStackEntry?.savedStateHandle
                            ?.set(ADD_EDIT_EXPENSE_RESULT, RESULT_EXPENSE_ADDED)
                        navController.popBackStack()
                    }
                    AddEditExpenseViewModel.AddEditEvents.ExpenseDeleted -> {
                        keyboardController?.hide()
                        navController.previousBackStackEntry?.savedStateHandle
                            ?.set(ADD_EDIT_EXPENSE_RESULT, RESULT_EXPENSE_DELETED)
                        navController.popBackStack()
                    }
                    AddEditExpenseViewModel.AddEditEvents.ExpenseUpdated -> {
                        keyboardController?.hide()
                        navController.previousBackStackEntry?.savedStateHandle
                            ?.set(ADD_EDIT_EXPENSE_RESULT, RESULT_EXPENSE_UPDATED)
                        navController.popBackStack()
                    }
                    is AddEditExpenseViewModel.AddEditEvents.ShowSnackbar -> {
                        snackbarController.showSnackbar(event.message.asString(context))
                    }
                }.exhaustive
            }
        }

        AddEditExpenseScreenContent(
            snackbarController = snackbarController,
            isEditMode = viewModel.editMode,
            amount = amount,
            name = name,
            newTagInput = newTagInput,
            state = state,
            actions = viewModel,
            navigateUp = navController::popBackStack
        )
    }
}

private const val ARG_EXPENSE_ID = "expenseId"
private const val NO_EXPENSE_ID = -1L