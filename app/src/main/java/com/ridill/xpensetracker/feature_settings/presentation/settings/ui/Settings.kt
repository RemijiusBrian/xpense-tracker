package com.ridill.xpensetracker.feature_settings.presentation.settings.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
            SectionTitle(title = R.string.general)
            PreferencesItem(
                title = stringResource(R.string.theme),
                summary = state.currentAppTheme?.label?.let { stringResource(it) },
                onClick = actions::onAppThemePreferenceClick,
                icon = Icons.Default.BrightnessMedium
            )
            ToggleablePreference(
                title = R.string.include_cash_flow_in_expenditure,
                isChecked = state.cashFlowIncludedInExpenditure,
                onCheckedChange = actions::onIncludeCashFlowInExpenditureCheckedChange
            )
        }

        // App Theme Selection Dialog
        if (state.showAppThemeDialog) {
            ThemeSelectionDialog(
                selectedTheme = state.currentAppTheme,
                onSelect = actions::onAppThemeDialogConfirm,
                onDismiss = actions::onAppThemeDialogDismiss
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
        style = MaterialTheme.typography.body2,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.primary,
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
private fun ToggleablePreference(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    @StringRes description: Int? = null,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { onCheckedChange(!isChecked) })
            .padding(vertical = PaddingSmall, horizontal = PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(title),
                maxLines = 1,
            )
            if (description != null) {
                Text(
                    text = stringResource(description),
                    color = Color.Gray,
                    style = MaterialTheme.typography.body2
                )
            }
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun ThemeSelectionDialog(
    selectedTheme: AppTheme?,
    onSelect: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.theme)) },
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