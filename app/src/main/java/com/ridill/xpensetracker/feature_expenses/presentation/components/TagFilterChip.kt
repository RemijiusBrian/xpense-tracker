package com.ridill.xpensetracker.feature_expenses.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import com.ridill.xpensetracker.R
import kotlinx.coroutines.delay

@Composable
fun TagFilterChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    deleteMode: Boolean,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(deleteMode) {
        if (deleteMode) {
            do {
                repeat(SHAKE_COUNT) {
                    rotation.animateTo(5f, animationSpec = tween(SHAKE_ANIM_DURATION))
                    rotation.animateTo(-5f, animationSpec = tween(SHAKE_ANIM_DURATION))
                }
                rotation.animateTo(0f)
                delay(SHAKE_DELAY)
            } while (deleteMode)
        }
    }
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(label)
        },
        trailingIcon = if (deleteMode) {
            {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .height(FilterChipDefaults.Height)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.content_description_delete_expense)
                    )
                }
            }
        } else null,
        modifier = modifier
            .rotate(rotation.value)
    )
}

private const val SHAKE_COUNT = 2
private const val SHAKE_ANIM_DURATION = 10
private const val SHAKE_DELAY = 5000L