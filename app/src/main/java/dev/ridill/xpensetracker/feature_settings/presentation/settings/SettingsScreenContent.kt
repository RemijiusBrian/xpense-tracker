package dev.ridill.xpensetracker.feature_settings.presentation.settings

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.BrightnessMedium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.net.toUri
import dev.ridill.xpensetracker.BuildConfig
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.domain.model.AppTheme
import dev.ridill.xpensetracker.core.ui.components.BackArrowButton
import dev.ridill.xpensetracker.core.ui.components.RadioButtonWithLabel
import dev.ridill.xpensetracker.core.ui.components.TransparentSmallTopAppBar
import dev.ridill.xpensetracker.core.ui.navigation.screen_specs.SettingsScreenSpec
import dev.ridill.xpensetracker.core.ui.theme.ContentAlpha
import dev.ridill.xpensetracker.core.ui.theme.SpacingMedium
import dev.ridill.xpensetracker.core.ui.theme.WEIGHT_1
import dev.ridill.xpensetracker.core.util.Constants
import dev.ridill.xpensetracker.core.util.isBuildVersionAndroid12OrAbove
import dev.ridill.xpensetracker.feature_settings.presentation.components.ExpenditureLimitUpdateDialog

@Composable
fun SettingsScreenContent(
    state: SettingsState,
    actions: SettingsActions,
    navigateUp: () -> Unit
) {
    val isDynamicThemeSupported = remember { isBuildVersionAndroid12OrAbove() }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TransparentSmallTopAppBar(
                title = SettingsScreenSpec.label,
                navigationIcon = { BackArrowButton(onClick = navigateUp) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // General Section
            SectionTitle(title = R.string.general)
            BasicPreference(
                title = R.string.theme,
                summary = stringResource(state.appTheme.label),
                onClick = actions::onThemePreferenceClick
            )
            SwitchPreference(
                title = R.string.dynamic_theme,
                checked = state.useDynamicTheme,
                onCheckedChange = actions::onUseDynamicCheckedChange,
                summary = stringResource(
                    if (isDynamicThemeSupported) R.string.dynamic_theme_summary
                    else R.string.dynamic_theme_unsupported_message
                ),
                enabled = isDynamicThemeSupported
            )

            // Expense Section
            SectionTitle(title = R.string.expense)
            BasicPreference(
                title = R.string.expenditure_limit,
                summary = state.expenditureLimit,
                onClick = actions::onExpenditureLimitPreferenceClick
            )

            // Links Section
            val context = LocalContext.current
            SectionTitle(title = R.string.links)
            BasicPreference(
                title = R.string.contact_support,
                summary = stringResource(R.string.bug_report_feature_request),
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:ridilldev@gmail.com".toUri()
                        putExtra(Intent.EXTRA_SUBJECT, "Xpense Tracker Feature Request/Bug Report")
                    }
                    context.startActivity(intent)
                }
            )

            // Info Section
            SectionTitle(title = R.string.info)
            BasicPreference(
                title = R.string.app_version,
                summary = BuildConfig.VERSION_NAME,
                icon = Icons.Default.Info
            )
        }

        if (state.showThemeSelection) {
            ThemeSelectionDialog(
                selectedTheme = state.appTheme,
                onDismiss = actions::onAppThemeSelectionDismiss,
                onConfirm = actions::onAppThemeSelectionConfirm
            )
        }

        if (state.showExpenditureUpdate) {
            ExpenditureLimitUpdateDialog(
                previousLimit = state.expenditureLimit,
                onDismiss = actions::onExpenditureLimitUpdateDismiss,
                onConfirm = actions::onExpenditureLimitUpdateConfirm
            )
        }
    }
}

@Composable
private fun SectionTitle(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(horizontal = SpacingMedium)
            .padding(top = SpacingMedium)
    )
}

@Composable
private fun BasicPreference(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    summary: String? = null,
    onClick: (() -> Unit)? = null,
    icon: ImageVector? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(SpacingMedium)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(Modifier.width(SpacingMedium))
        }
        Column {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.bodyLarge
            )
            if (summary != null) {
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                        .copy(alpha = ContentAlpha.PERCENT_60)
                )
            }
        }
    }
}

@Composable
private fun SwitchPreference(
    @StringRes title: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (enabled) Modifier.clickable { onCheckedChange(!checked) } else Modifier)
            .padding(SpacingMedium)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(Modifier.width(SpacingMedium))
        }
        Column(Modifier.weight(WEIGHT_1)) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
                    .copy(
                        alpha = if (enabled) Constants.ONE_F
                        else ContentAlpha.PERCENT_16
                    )
            )
            if (summary != null) {
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                        .copy(
                            alpha = if (enabled) ContentAlpha.PERCENT_60
                            else ContentAlpha.PERCENT_16
                        )
                )
            }
        }
        Spacer(Modifier.width(SpacingMedium))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Composable
private fun ThemeSelectionDialog(
    selectedTheme: AppTheme,
    onDismiss: () -> Unit,
    onConfirm: (AppTheme) -> Unit
) {
    var selection by remember { mutableStateOf(selectedTheme) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onConfirm(selection) }) {
                Text(stringResource(R.string.action_confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
        title = { Text(text = stringResource(R.string.select_theme)) },
        text = {
            Column {
                AppTheme.values().forEach { theme ->
                    RadioButtonWithLabel(
                        label = theme.label,
                        selected = theme == selection,
                        onClick = { selection = theme },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        },
        icon = {
            Icon(imageVector = Icons.Outlined.BrightnessMedium, contentDescription = null)
        }
    )
}