package com.ridill.xpensetracker.feature_expense_plan.presentation.expense_plans.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.theme.SpacingSmall

@Composable
fun ExpensePlans() {
    ScreenContent()
}

@Composable
private fun ScreenContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.Timer, contentDescription = null)
            Spacer(modifier = Modifier.height(SpacingSmall))
            Text(stringResource(R.string.coming_soon))
        }
    }
}