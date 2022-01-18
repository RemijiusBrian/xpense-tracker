package com.ridill.xpensetracker.core.presentation.util

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import kotlin.math.roundToInt

private const val DEFAULT_OFFSET_RATIO = 0.3f
private const val DEFAULT_DURATION = 300

fun slideInHorizontallyWithFadeIn(
    fromRight: Boolean = true,
    offsetRatio: Float = DEFAULT_OFFSET_RATIO,
    duration: Int = DEFAULT_DURATION
): EnterTransition = slideInHorizontally(
    initialOffsetX = { (it * if (fromRight) offsetRatio else -offsetRatio).roundToInt() },
    animationSpec = tween(durationMillis = duration)
) + fadeIn(animationSpec = tween(durationMillis = duration))

fun slideOutHorizontallyWithFadeOut(
    toRight: Boolean = true,
    offsetRatio: Float = DEFAULT_OFFSET_RATIO,
    duration: Int = DEFAULT_DURATION
): ExitTransition = slideOutHorizontally(
    targetOffsetX = { (it * if (toRight) offsetRatio else -offsetRatio).roundToInt() },
    animationSpec = tween(durationMillis = duration)
) + fadeOut(animationSpec = tween(durationMillis = duration))

fun <T> AnimatedContentScope<T>.numberSliderTransition(
    condition: () -> Boolean = { true },
): ContentTransform = if (condition()) {
    slideInVertically { height -> height } + fadeIn() with
            slideOutVertically { height -> -height } + fadeOut()
} else {
    slideInVertically { height -> -height } + fadeIn() with
            slideOutVertically { height -> height } + fadeOut()
} using SizeTransform(clip = false)