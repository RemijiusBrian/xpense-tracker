package com.ridill.xpensetracker.core.ui.navigation

import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ridill.xpensetracker.R

sealed class XTDestination(
    val route: String,
    @StringRes val label: Int,
    val arguments: List<NamedNavArgument> = emptyList(),
) {
    object Expenses : XTDestination(
        route = "expenses",
        label = R.string.destination_expenses,
    )

    object AddEditExpense : XTDestination(
        route = "addEditExpense",
        label = R.string.destination_add_edit_expense,
        arguments = listOf(
            navArgument(ARG_EXPENSE_ID) {
                type = NavType.LongType
                nullable = false
                defaultValue = INVALID_ID
            }
        )
    ) {
        val navRoute get() = "$route?$ARG_EXPENSE_ID={$ARG_EXPENSE_ID}"
        fun withArg(expenseId: Long): String = "$route?$ARG_EXPENSE_ID=$expenseId"
    }
}

const val ARG_EXPENSE_ID = "ARG_EXPENSE_ID"

const val INVALID_ID = -1L