package com.ridill.xpensetracker.core.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TransparentTopAppBar(
    title: String? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { title?.let { Text(it) } },
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )
}