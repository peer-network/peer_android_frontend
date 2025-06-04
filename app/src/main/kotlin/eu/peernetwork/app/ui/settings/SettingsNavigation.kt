package eu.peernetwork.app.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.about.AboutScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.password.update.PasswordUpdateScreen
import eu.peernetwork.user.ui.settings.account.AccountScreen
import eu.peernetwork.user.ui.settings.address.AddressScreen

@Composable
fun SettingsNavigation(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    settings: @Composable (NavHostController) -> Unit
) {
    val controller = rememberNavController()
    val updatedSettings by rememberUpdatedState(settings)
    val password = stringResource(R.string.password_label)
    val preference = stringResource(R.string.preference_label)
    val account = stringResource(R.string.account_label)
    DesignRouter(
        navController = controller,
        startDestination = "settings",
    ) {
        composable("settings") { updatedSettings(controller) }
        composable(account) { AccountScreen(provider, viewModelStoreOwner) }
        composable(password) {
            PasswordUpdateScreen(provider) {
                controller.popBackStack()
            }
        }
        composable(preference) {
            AddressScreen(provider) {
                controller.popBackStack()
            }
        }
        composable("about") {
            AboutScreen(
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE,
                provider,
                stringResource(R.string.about_us_label),
            )
        }
    }
}
