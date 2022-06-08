package com.ridill.xpensetracker.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun SearchView(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes placeholder: Int? = null,
    textColor: Color = Color.White,
    onClearQueryClick: () -> Unit,
    onDone: () -> Unit = {}
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        cursorBrush = SolidColor(textColor),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ),
        textStyle = TextStyle.Default.copy(color = textColor)
    ) { innerTextField ->
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                this@Row.AnimatedVisibility(placeholder != null && query.isEmpty()) {
                    Text(
                        stringResource(placeholder!!),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor.copy(alpha = DefaultAlpha)
                    )
                }
                innerTextField()
            }
            AnimatedVisibility(query.isNotEmpty()) {
                IconButton(onClick = onClearQueryClick) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }
            }
        }
    }
}