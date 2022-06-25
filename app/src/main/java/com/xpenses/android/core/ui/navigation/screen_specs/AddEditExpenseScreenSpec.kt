package com.xpenses.android.core.ui.navigation.screen_specs

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.*
import com.xpenses.android.R
import com.xpenses.android.feature_expenses.presentation.add_edit_expense.AddEditExpenseScreen

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

    fun isNewExpenseMode(expenseId: Long?): Boolean = expenseId != null && expenseId != NO_EXPENSE_ID

    fun buildRoute(expenseId: Long = NO_EXPENSE_ID): String =
        "add_edit_expense?$ARG_EXPENSE_ID=$expenseId"

    fun getExpenseIdFromSavedStateHandle(savedStateHandle: SavedStateHandle): Long? =
        savedStateHandle.get<Long>(ARG_EXPENSE_ID)

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        AddEditExpenseScreen(navController = navController)
    }
}

private const val ARG_EXPENSE_ID = "expenseId"
private const val NO_EXPENSE_ID = -1L