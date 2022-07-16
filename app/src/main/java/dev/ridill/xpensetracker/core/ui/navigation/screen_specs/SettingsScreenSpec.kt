package dev.ridill.xpensetracker.core.ui.navigation.screen_specs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.*
import dev.ridill.xpensetracker.R
import dev.ridill.xpensetracker.core.ui.components.rememberSnackbarController
import dev.ridill.xpensetracker.core.util.exhaustive
import dev.ridill.xpensetracker.feature_settings.presentation.settings.SettingsScreenContent
import dev.ridill.xpensetracker.feature_settings.presentation.settings.SettingsState
import dev.ridill.xpensetracker.feature_settings.presentation.settings.SettingsViewModel

object SettingsScreenSpec : BottomBarScreenSpec {

    override val icon: ImageVector = Icons.Outlined.Settings

    override val label: Int = R.string.destination_settings

    override val navHostRoute: String = "settings?$ARG_SETTINGS_ACTION={$ARG_SETTINGS_ACTION}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(ARG_SETTINGS_ACTION) {
                type = NavType.StringType
                defaultValue = ARG_NO_ACTION
                nullable = false
            }
        )

    fun getActionFromSavedStateHandle(savedStateHandle: SavedStateHandle): String? =
        savedStateHandle.get<String>(ARG_SETTINGS_ACTION)

    fun buildRoute(action: String = ARG_NO_ACTION): String = "settings?$ARG_SETTINGS_ACTION=$action"

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        val viewModel: SettingsViewModel = hiltViewModel()
        val state by viewModel.state.observeAsState(SettingsState.INITIAL)

        val context = LocalContext.current
        val snackbarController = rememberSnackbarController()

        LaunchedEffect(context) {
            viewModel.events.collect { event ->
                when (event) {
                    is SettingsViewModel.SettingsEvent.ShowErrorMessage -> {
                        snackbarController.showErrorSnackbar(event.message.asString(context))
                    }
                    is SettingsViewModel.SettingsEvent.ShowUiMessage -> {
                        snackbarController.showMessageSnackbar(event.message.asString(context))
                    }
                }.exhaustive
            }
        }

        SettingsScreenContent(
            snackbarController = snackbarController,
            state = state,
            actions = viewModel,
            navigateUp = navController::popBackStack
        )
    }
}

private const val ARG_SETTINGS_ACTION = "ARG_SETTINGS_ACTION"
private const val ARG_NO_ACTION = "ARG_NO_ACTION"
const val SETTINGS_ACTION_EXPENDITURE_LIMIT_UPDATE = "SETTINGS_ACTION_EXPENDITURE_LIMIT_UPDATE"