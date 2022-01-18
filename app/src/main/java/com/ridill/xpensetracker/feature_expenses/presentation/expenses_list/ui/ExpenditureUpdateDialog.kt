package com.ridill.xpensetracker.feature_expenses.presentation.expenses_list.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ridill.xpensetracker.R

@Composable
fun ExpenditureUpdateDialog(
    onDismiss: () -> Unit,
    limit: String,
    onConfirm: (String) -> Unit,
) {
    var amount by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.update_expenditure_limit)) },
        text = {
            Column(
                modifier = Modifier,
            ) {
                Text(
                    text = stringResource(R.string.update_expenditure_limit_message),
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = { Text(text = limit) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onConfirm(amount) }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(amount) }) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}