package eu.peernetwork.app.ui.setup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.ui.privacy.PrivacyScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.design.compose.DesignTitleBar
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
                PrivacyScreen { controller.popBackStack() }
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
    }
}
