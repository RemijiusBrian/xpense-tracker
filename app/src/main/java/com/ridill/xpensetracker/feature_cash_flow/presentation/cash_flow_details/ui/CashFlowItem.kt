package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_details.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall
import com.ridill.xpensetracker.core.ui.theme.ZeroDp
import com.ridill.xpensetracker.core.ui.util.numberSliderTransition

@Composable
fun CashFlowItem(
    modifier: Modifier = Modifier,
    name: String,
    amount: String,
    date: String,
    lending: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingSmall)
            .padding(top = PaddingSmall),
        elevation = ZeroDp
    ) {
        Row(
            modifier = Modifier
                .padding(PaddingMedium),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.caption
                )
            }
            Column {
                AnimatedContent(
                    targetState = amount,
                    transitionSpec = { numberSliderTransition { targetState > initialState } }
                ) {
                    Text(
                        text = amount,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = stringResource(
                        if (lending) R.string.lent
                        else R.string.borrowed
                    ),
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}