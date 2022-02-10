package com.ridill.xpensetracker.feature_cash_flow.presentation.cash_flow.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.ui.theme.PaddingExtraSmall
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall

@Composable
fun AgentItem(
    modifier: Modifier = Modifier,
    name: String,
    pending: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = PaddingSmall)
            .padding(top = PaddingSmall)
            .fillMaxWidth()
            .height(CardHeight)
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primary.copy(alpha = 0.88f)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(PaddingMedium)
            )
            if (pending) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = stringResource(R.string.payment_pending),
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(PaddingExtraSmall)
                        .size(ClearedStatusIconSize)
                )
            }
        }
    }
}

private val ClearedStatusIconSize = 16.dp
private val CardHeight = 80.dp