package com.ridill.xpensetracker.feature_expenses.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Umbrella
import com.ridill.xpensetracker.R

enum class ExpenseCategory(
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    EXPENSE(
        icon = R.drawable.ic_cost,
        label = R.string.expense
    ),
    CASH_FLOW(
        icon = R.drawable.ic_cash_flow,
        label = R.string.cash_flow
    ),
    YEARNING(
        icon = R.drawable.ic_coming_soon,
        label = R.string.yearning
    )
}