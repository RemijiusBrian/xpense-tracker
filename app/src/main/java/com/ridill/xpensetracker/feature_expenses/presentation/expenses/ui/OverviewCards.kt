package com.ridill.xpensetracker.feature_expenses.presentation.expenses.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.theme.*
import com.ridill.xpensetracker.core.ui.util.numberSliderTransition

@Composable
fun OverviewCards(
    modifier: Modifier = Modifier,
    expenditureLimit: String,
    currentExpenditure: String,
    spendingBalance: String,
    balancePercent: Float,
    onEditLimitClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExpenditureLimit(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            limit = expenditureLimit,
            onEditClick = onEditLimitClick
        )
        if (expenditureLimit.isNotEmpty()) {
            Spacer(modifier = Modifier.width(SpacingSmall))
            CurrentExpenditureAndSpendingBalance(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                expenditure = currentExpenditure,
                balance = spendingBalance,
                balancePercent = balancePercent,
            )
        }
    }
}

// Expenditure Limit
@Composable
private fun ExpenditureLimit(
    modifier: Modifier = Modifier,
    limit: String,
    onEditClick: () -> Unit
) {
    Card(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colors.primaryVariant.copy(alpha = 0.48f),
                            MaterialTheme.colors.primary
                        ),
                    ),
                    shape = MaterialTheme.shapes.medium
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingMedium),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedContent(targetState = limit) { limit ->
                    Text(
                        text = limit.ifEmpty { stringResource(R.string.track_your_expenses) },
                        style = if (limit.isNotEmpty()) MaterialTheme.typography.h4
                        else MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                AnimatedVisibility(visible = limit.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.expenditure_limit),
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(EditIconSize)
                    .padding(PaddingSmall)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.update_expenditure_limit),
                )
            }
        }
    }
}

private val EditIconSize = 36.dp

// Current Expenditure And Balance
@Composable
private fun CurrentExpenditureAndSpendingBalance(
    modifier: Modifier = Modifier,
    expenditure: String,
    balance: String,
    balancePercent: Float,
) {
    Column(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colors.primary,
                                MaterialTheme.colors.primaryVariant.copy(alpha = 0.48f),
                            ),
                        ),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(PaddingSmall)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedContent(
                    targetState = expenditure,
                    transitionSpec = { numberSliderTransition { targetState > initialState } }
                ) { expenditure ->
                    Text(
                        text = expenditure.ifEmpty { stringResource(id = R.string.current_expenditure) },
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Text(
                    text = stringResource(R.string.current_expenditure),
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(SpacingExtraSmall))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            border = BorderStroke(
                width = XTBorderWidth,
                color = MaterialTheme.colors.onPrimary
            )
        ) {
            val progress by animateFloatAsState(targetValue = balancePercent)
            val backgroundColor by animateColorAsState(
                targetValue = if (balancePercent <= 0f) MaterialTheme.colors.error
                else MaterialTheme.colors.surface
            )

            Box {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxSize(),
                    backgroundColor = backgroundColor,
                    color = MaterialTheme.colors.primaryVariant
                )
                Column(
                    modifier = Modifier
                        .padding(PaddingSmall)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    AnimatedContent(
                        targetState = balance,
                        transitionSpec = { numberSliderTransition { targetState > initialState } }
                    ) { balance ->
                        Text(
                            text = balance.ifEmpty { stringResource(R.string.balance) },
                            style = MaterialTheme.typography.subtitle1,
                        )
                    }
                    Text(
                        text = stringResource(R.string.balance),
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}