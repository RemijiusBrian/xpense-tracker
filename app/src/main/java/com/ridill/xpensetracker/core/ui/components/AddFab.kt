package com.ridill.xpensetracker.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun AddFab(
    onClick: () -> Unit,
    @StringRes contentDescription: Int
) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = stringResource(contentDescription)
        )
    }
}