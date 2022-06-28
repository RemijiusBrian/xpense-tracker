package dev.ridill.xpensetracker.feature_bills.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ridill.xpensetracker.core.ui.theme.ContentAlpha
import dev.ridill.xpensetracker.core.ui.theme.PaddingMedium
import dev.ridill.xpensetracker.feature_bills.domain.model.BillCategory

@Composable
fun BillCategoryIcon(
    category: BillCategory,
    modifier: Modifier = Modifier,
    size: Dp = DefaultSize
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant
                    .copy(alpha = ContentAlpha.PERCENT_16),
                shape = MaterialTheme.shapes.small
            )
            .padding(PaddingMedium)
    ) {
        Icon(
            painter = painterResource(category.icon),
            contentDescription = stringResource(category.label),
            modifier = Modifier
                .size(size),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private val DefaultSize = 24.dp