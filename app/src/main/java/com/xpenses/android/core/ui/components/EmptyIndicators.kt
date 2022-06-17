package com.xpenses.android.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xpenses.android.R
import com.xpenses.android.core.ui.theme.SpacingXSmall
import kotlinx.coroutines.delay

@Composable
fun ListEmptyIndicator(
    modifier: Modifier = Modifier,
    @StringRes message: Int = R.string.default_list_empty_message
) {
    val animationValues = (1..3).map { index ->
        var offset by remember { mutableStateOf(0f) }

        LaunchedEffect(Unit) {
            delay(DELAY * index)
            animate(
                initialValue = 0f,
                targetValue = -12f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = DURATION,
                        delayMillis = INTERVAL_DURATION
                    ),
                    repeatMode = RepeatMode.Reverse,
                )
            ) { animatedValue, _ -> offset = animatedValue }
        }
        offset
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        animationValues.forEach { animationValue ->
            Box(
                modifier = Modifier
                    .offset(y = animationValue.dp)
                    .width(ListBarWidth)
                    .height(ListBarHeight)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.height(SpacingXSmall))
        }
        Spacer(modifier = Modifier.height(SpacingXSmall))
        Text(
            text = stringResource(message),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

private val ListBarWidth = 24.dp
private val ListBarHeight = 4.dp
private const val DURATION = 500
private const val DELAY = 100L
private const val INTERVAL_DURATION = 5_000