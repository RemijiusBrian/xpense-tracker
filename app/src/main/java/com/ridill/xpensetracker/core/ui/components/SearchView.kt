package com.ridill.xpensetracker.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.ridill.xpensetracker.core.ui.theme.PaddingMedium
import com.ridill.xpensetracker.core.ui.theme.PaddingSmall

@Composable
fun SearchView(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes placeholder: Int? = null,
    onClearQueryClick: () -> Unit,
    onDone: () -> Unit = {}
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .padding(horizontal = PaddingMedium, vertical = PaddingSmall),
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty()) {
                IconButton(onClick = onClearQueryClick) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
        ),
        placeholder = {
            placeholder?.let { Text(stringResource(it)) }
        },
        singleLine = true,
    )
}