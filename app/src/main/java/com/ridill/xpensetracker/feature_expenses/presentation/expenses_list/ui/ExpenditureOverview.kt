package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall
import com.ridill.xpensetracker.core.ui.theme.SpacingExtraSmall
import com.ridill.xpensetracker.core.ui.util.numberSliderTransition

@Composable
fun ExpenditureOverview(
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
                                MaterialTheme.colors.primaryVariant.copy(alpha = 0.48f)
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
                    )
                }
                Text(
                    text = stringResource(R.string.current_expenditure),
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Spacer(modifier = Modifier.height(SpacingExtraSmall))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
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