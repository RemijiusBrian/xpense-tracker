package com.ridill.xpensetracker.feature_dashboard.presentation.dashboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall
import com.ridill.xpensetracker.core.ui.theme.SpacingExtraSmall
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseCategory

@Composable
fun ExpenseItem(
    modifier: Modifier = Modifier,
    name: String,
    amount: String,
    date: String,
    category: ExpenseCategory,
    onClick: () -> Unit,
    onSwipeDeleted: () -> Unit
) {
    if (category == ExpenseCategory.CASH_FLOW) {
        ExpenseDetailsCard(
            modifier = modifier,
            name = name,
            amount = amount,
            date = date,
            onClick = onClick
        )
    } else {
        val dismissState = rememberDismissState(
            confirmStateChange = {
                if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                    onSwipeDeleted()
                }
                true
            }
        )

        SwipeToDismiss(
            state = dismissState,
            background = {},
            modifier = modifier
        ) {
            ExpenseDetailsCard(
                name = name,
                amount = amount,
                date = date,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun ExpenseDetailsCard(
    modifier: Modifier = Modifier,
    name: String,
    amount: String,
    date: String,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = PaddingSmall),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = PaddingSmall, horizontal = PaddingMedium),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = amount,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(SpacingExtraSmall))
            Text(
                text = date,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}