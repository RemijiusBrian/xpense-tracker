package com.xpenses.android.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.xpenses.android.R

@Composable
fun SimpleConfirmationDialog(
    @StringRes title: Int,
    @StringRes text: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    icon: ImageVector? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            icon?.let { Icon(imageVector = it, contentDescription = null) }
        },
        title = { Text(stringResource(title)) },
        text = { Text(stringResource(text)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.action_confirm))
            }
        },
        dismissButton = {
            OutlinedIconButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}