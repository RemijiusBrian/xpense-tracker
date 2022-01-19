package com.ridill.xpensetracker.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SwipeDismissBackground(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.error,
    icon: ImageVector? = null
) {
    Surface(
        modifier = modifier,
        color = color
    ) {
        icon?.let { imageVector ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(imageVector = imageVector, contentDescription = null)
                Icon(imageVector = imageVector, contentDescription = null)
            }
        }
    }
}