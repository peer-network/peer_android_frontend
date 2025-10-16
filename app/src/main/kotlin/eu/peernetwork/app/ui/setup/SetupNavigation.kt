package eu.peernetwork.app.ui.setup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.about.AboutScreen
import eu.peernetwork.app.ui.browser.BrowserScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.design.material.DesignTitleBar
import eu.peernetwork.user.ui.password.request.PasswordRequestScreen

@Composable
fun SetupNavigation(
    provider: UiComponentProvider,
    setup: @Composable (NavHostController) -> Unit
) {
    val controller = rememberNavController()
    val updatedSetup by rememberUpdatedState(setup)
    DesignRouter(
        navController = controller,
        startDestination = "setup",
    ) {
        composable("setup") { updatedSetup(controller) }
        composable("privacy") {
            DesignTitleBar {
                BrowserScreen(BuildConfig.PRIVACY_POLICY_URL) { controller.popBackStack() }
            }
        }
        composable(
            "passwordRequest/{email}",
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            PasswordRequestScreen(
                backStackEntry.arguments?.getString("email"),
                provider
            ) { controller.popBackStack() }
        }
        composable("about") {
            Box(modifier = Modifier.padding(vertical = 24.dp)) {
                AboutScreen(
                    BuildConfig.VERSION_NAME,
                    BuildConfig.VERSION_CODE,
                    provider
                )
            }
        }
    }
}
