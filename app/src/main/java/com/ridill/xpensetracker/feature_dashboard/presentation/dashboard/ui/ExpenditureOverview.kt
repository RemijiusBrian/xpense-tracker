package com.ridill.xpensetracker.feature_dashboard.presentation.dashboard.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall
import com.ridill.xpensetracker.core.ui.theme.SpacingExtraSmall
import com.ridill.xpensetracker.core.ui.theme.SpacingSmall
import com.ridill.xpensetracker.core.ui.util.numberSliderTransition

@Composable
fun ExpenditureOverview(
    modifier: Modifier = Modifier,
    expenditureLimit: String,
    onExpenditureLimitUpdate: () -> Unit,
    currentExpenditure: String,
    balance: String,
    balancePercent: Float,
    isBalanceEmpty: Boolean
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
            onClick = onExpenditureLimitUpdate
        )
        Spacer(modifier = Modifier.width(SpacingSmall))
        CurrentExpenditureAndBalance(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            expenditure = currentExpenditure,
            balance = balance,
            balancePercent = balancePercent,
            isBalanceEmpty = isBalanceEmpty
        )
    }
}

// Expenditure Limit
@Composable
private fun ExpenditureLimit(
    modifier: Modifier = Modifier,
    limit: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colors.primaryVariant.copy(alpha = 0.48f),
                            MaterialTheme.colors.primary
                        ),
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(PaddingMedium),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            AnimatedContent(targetState = limit) { limit ->
                Text(
                    text = limit,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.onPrimary
                )
            }
            Text(
                text = stringResource(R.string.expenditure_limit),
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
private fun CurrentExpenditureAndBalance(
    modifier: Modifier = Modifier,
    expenditure: String,
    balance: String,
    balancePercent: Float,
    isBalanceEmpty: Boolean,
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
            ) {
                AnimatedContent(
                    targetState = expenditure,
                    transitionSpec = { numberSliderTransition { targetState > initialState } }
                ) { expenditure ->
                    Text(
                        text = expenditure,
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
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.onPrimary)
        ) {
            val transition = updateTransition(
                targetState = balancePercent,
                label = stringResource(R.string.balance_animation)
            )
            val progress by transition.animateFloat(
                label = stringResource(R.string.balance_transition_width)
            ) { percentage -> percentage }
            val backgroundColor by animateColorAsState(
                targetValue = if (isBalanceEmpty) MaterialTheme.colors.error
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
                ) {
                    AnimatedContent(
                        targetState = balance,
                        transitionSpec = { numberSliderTransition { targetState > initialState } }
                    ) { balance ->
                        Text(
                            text = balance,
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