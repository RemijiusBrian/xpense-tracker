package com.ridill.xpensetracker.feature_expenses.domain.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.ui.graphics.vector.ImageVector
import com.ridill.xpensetracker.R

enum class ExpenseCategory(
    val icon: ImageVector,
    @StringRes val label: Int
) {
    EXPENSE(
        icon = Icons.Default.AttachMoney,
        label = R.string.expense
    ),
    CASH_FLOW(
        icon = Icons.Default.AccountBalanceWallet,
        label = R.string.cash_flow
    ),
    YEARNING(
        icon = Icons.Default.PersonPin,
        label = R.string.yearning
    )
}