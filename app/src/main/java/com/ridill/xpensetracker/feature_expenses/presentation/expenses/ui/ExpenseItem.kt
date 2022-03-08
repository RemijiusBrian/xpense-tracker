package com.ridill.xpensetracker.feature_expenses.presentation.expenses.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall
import com.ridill.xpensetracker.core.ui.theme.SpacingExtraSmall

@Composable
fun ExpenseItem(
    modifier: Modifier = Modifier,
    name: String,
    amount: String,
    date: String,
    isMonthly: Boolean,
    onClick: () -> Unit,
    onSwipeDeleted: () -> Unit,
    tag: String?
) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd) {
                onSwipeDeleted()
            }
            true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colors.primaryVariant)
                    .padding(horizontal = PaddingMedium),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }
        },
        modifier = modifier
            .padding(top = PaddingSmall),
        directions = setOf(DismissDirection.StartToEnd)
    ) {
        Card(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth(),
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = PaddingSmall, horizontal = PaddingMedium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.h6,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(SpacingExtraSmall))
                    Text(
                        text = if (isMonthly) stringResource(R.string.monthly) else date,
                        style = MaterialTheme.typography.caption,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = amount,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    tag?.let {
                        Spacer(modifier = Modifier.height(SpacingExtraSmall))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.overline,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseDateSeparator(
    modifier: Modifier = Modifier,
    date: String,
    onClick: (String) -> Unit
) {
    TextButton(
        onClick = { onClick(date) },
        modifier = modifier
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold,
        )
    }
}