package com.ridill.xpensetracker.feature_settings.presentation.settings.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ridill.xpensetracker.R
import com.ridill.xpensetracker.core.domain.model.AppTheme
import com.ridill.xpensetracker.core.ui.components.BackArrowButton
import com.ridill.xpensetracker.core.ui.components.InputDialog
import com.ridill.xpensetracker.core.ui.navigation.Destination
import com.ridill.xpensetracker.core.ui.theme.*
import com.ridill.xpensetracker.feature_settings.presentation.settings.SettingsActions
import com.ridill.xpensetracker.feature_settings.presentation.settings.SettingsState
import com.ridill.xpensetracker.feature_settings.presentation.settings.SettingsViewModel

@Composable
fun Settings(
    navController: NavController
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val state by viewModel.state.observeAsState(SettingsState.INITIAL)

    ScreenContent(
        state = state,
        actions = viewModel,
        navigateBack = navController::popBackStack
    )
}

@Composable
private fun ScreenContent(
    state: SettingsState,
    actions: SettingsActions,
    navigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Destination.Settings.label)) },
                navigationIcon = { BackArrowButton(onClick = navigateBack) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            SectionTitle(title = R.string.xpense)
            PreferencesItem(
                title = stringResource(R.string.expenditure_limit),
                summary = state.currentExpenditureLimit,
                onClick = actions::onExpenditureLimitPreferenceClick,
                icon = Icons.Default.AccountBalanceWallet
            )
            Divider()
            Spacer(modifier = Modifier.height(SpacingSmall))
            SectionTitle(title = R.string.general)
            PreferencesItem(
                title = stringResource(R.string.theme),
                summary = state.currentAppTheme?.label?.let { stringResource(it) },
                onClick = actions::onAppThemePreferenceClick,
                icon = Icons.Default.BrightnessMedium
            )
            Divider()
        }

        // App Theme Selection Dialog
        if (state.showAppThemeDialog) {
            AppThemeSelectionDialog(
                selectedTheme = state.currentAppTheme,
                onSelect = actions::onAppThemeDialogConfirm,
                onDismiss = actions::onAppThemeDialogDismiss
            )
        }

        // Expenditure Limit Dialog
        if (state.showExpenditureLimitDialog) {
            InputDialog(
                title = R.string.update_expenditure_limit,
                message = R.string.update_expenditure_limit_message,
                placeholder = state.currentExpenditureLimit,
                onDismiss = actions::onExpenditureLimitDialogDismiss,
                onConfirm = actions::onExpenditureLimitDialogConfirm
            )
        }
    }
}

@Composable
private fun SectionTitle(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
) {
    Text(
        stringResource(title),
        style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
        modifier = modifier
            .padding(horizontal = PaddingMedium)
            .padding(top = PaddingSmall)
    )
}

@Composable
private fun PreferencesItem(
    modifier: Modifier = Modifier,
    title: String,
    summary: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(imageVector = it, contentDescription = null)
            Spacer(modifier = Modifier.width(SpacingMedium))
        }
        Column {
            Text(text = title)
            if (summary != null) {
                Text(
                    text = summary,
                    color = Color.Gray,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
private fun AppThemeSelectionDialog(
    selectedTheme: AppTheme?,
    onSelect: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_app_theme)) },
        text = {
            Column(
                modifier = Modifier
                    .padding(vertical = PaddingSmall)
            ) {
                AppTheme.values().forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(theme) }
                            .padding(horizontal = PaddingExtraSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = theme == selectedTheme,
                            onClick = { onSelect(theme) })
                        Spacer(modifier = Modifier.width(SpacingSmall))
                        Text(stringResource(theme.label))
                    }
                }
            }
        },
        confirmButton = {}
    )
}