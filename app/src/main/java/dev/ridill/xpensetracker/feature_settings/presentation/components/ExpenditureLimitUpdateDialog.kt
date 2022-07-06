package dev.ridill.xpensetracker.feature_settings.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import dev.ridill.xpensetracker.R

@Composable
fun ExpenditureLimitUpdateDialog(
    previousLimit: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onConfirm(input) }) {
                Text(stringResource(R.string.action_confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_piggy_bank),
                contentDescription = null
            )
        },
        title = {
            Text(stringResource(R.string.enter_expenditure_limit))
        },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                singleLine = true,
                placeholder = {
                    Text(previousLimit)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                shape = MaterialTheme.shapes.medium
            )
        }
    )
}
