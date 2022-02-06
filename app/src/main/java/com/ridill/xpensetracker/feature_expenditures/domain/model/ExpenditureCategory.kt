package com.ridill.xpensetracker.feature_expenditures.domain.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.ui.graphics.vector.ImageVector
import com.ridill.xpensetracker.R

enum class ExpenditureCategory(
    val icon: ImageVector,
    @StringRes val label: Int
) {
    EXPENDITURE(
        icon = Icons.Default.AttachMoney,
        label = R.string.expenditure
    ),
    CASH_FLOW(
        icon = Icons.Default.AccountBalanceWallet,
        label = R.string.cash_flow
    ),
    YEARNING(
        icon = Icons.Default.Umbrella,
        label = R.string.yearning
    )
}