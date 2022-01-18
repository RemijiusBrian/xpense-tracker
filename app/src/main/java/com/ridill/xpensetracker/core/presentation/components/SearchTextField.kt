package com.ridill.xpensetracker.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.presentation.ui.theme.PaddingSmall
import com.ridill.xpensetracker.core.presentation.ui.theme.SpacingMedium

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onDismiss: () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.02f),
                shape = RoundedCornerShape(50)
            ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colors.onSurface
        )
    ) { innerTextField ->
        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            if (text.isEmpty()) {
                Text(
                    text = stringResource(R.string.search),
                    modifier = Modifier
                        .padding(horizontal = PaddingSmall)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PaddingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                innerTextField()
                Spacer(modifier = Modifier.width(SpacingMedium))
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.dismiss)
                    )
                }
            }
        }
    }
}