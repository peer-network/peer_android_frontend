package eu.peernetwork.app.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.about.AboutScreen
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.app.ui.version.VersionScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.social.ui.referral.ReferralScreen
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.password.update.PasswordUpdateScreen
import eu.peernetwork.user.ui.account.AccountScreen
import eu.peernetwork.user.ui.email.EmailScreen

@Composable
fun SettingsNavigation(
    userId: String,
    provider: UiComponentProvider,
    settings: @Composable (NavBackStackEntry, NavHostController) -> Unit
) {
    val controller = rememberNavController()
    val updatedSettings by rememberUpdatedState(settings)
    val referral = stringResource(R.string.referral_name_label)
    val password = stringResource(R.string.password_label)
    val preference = stringResource(R.string.preference_label)
    val account = stringResource(R.string.account_label)
    DesignRouter(
        navController = controller,
        startDestination = "settings",
    ) {
        composable("settings") { updatedSettings(it, controller) }
        composable(account) { AccountScreen(provider, it) }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ProfileScreen(
                principal = userId,
                userId = id,
                provider = provider,
                viewModelStoreOwner = backStackEntry,
            )
        }
        composable(referral) { backStackEntry ->
            ReferralScreen(
                userId = userId,
                postLimit = BuildConfig.PAGING_LIMIT,
                provider = provider,
                viewModelStoreOwner = backStackEntry
            ) {
                controller.navigateIfNecessary("profile/${it.id}")
            }
        }
        composable(password) {
            PasswordUpdateScreen(provider) {
                controller.popBackStack()
            }
        }
        composable(preference) {
            EmailScreen(provider) {
                controller.popBackStack()
            }
        }
        composable("version") {
            VersionScreen(
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE,
                provider
            )
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
