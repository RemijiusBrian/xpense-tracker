package com.ridill.xpensetracker.feature_dashboard.presentation.dashboard.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory

@Composable
fun AddExpenseFab(
    currentCategory: ExpenseCategory,
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick) {
        AnimatedContent(targetState = currentCategory) { category ->
            Icon(
                imageVector = category.icon,
                contentDescription = stringResource(
                    R.string.add_category_placeholder, stringResource(category.label)
                )
            )
        }
    }
}