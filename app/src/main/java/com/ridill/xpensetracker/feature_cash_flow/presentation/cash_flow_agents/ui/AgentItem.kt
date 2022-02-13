package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow_agents.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall

@Composable
fun AgentItem(
    modifier: Modifier = Modifier,
    name: String,
    date: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = PaddingSmall)
            .padding(top = PaddingSmall)
            .fillMaxWidth()
            .height(CardHeight),
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.88f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingSmall),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = date,
                style = MaterialTheme.typography.overline,
                color = MaterialTheme.colors.onPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private val CardHeight = 80.dp