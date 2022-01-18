package com.ridill.xpensetracker.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun RadioButtonWithText(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(8.dp)
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(4.dp))
        Text(stringResource(text))
    }
}