package com.xpenses.android.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.xpenses.android.core.ui.theme.PaddingMedium
import com.xpenses.android.core.ui.theme.PaddingSmall

@Composable
fun ListLabel(
    @StringRes label: Int
) {
    Text(
        text = stringResource(label),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .paddingFromBaseline(
                top = PaddingMedium,
                bottom = PaddingSmall
            )
    )
}