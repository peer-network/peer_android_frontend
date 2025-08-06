package eu.peernetwork.app.ui.wallet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.model.ViewModelState

@Composable
fun WalletNavigation(
    id: String,
    provider: UiComponentProvider,
    viewModelStore: ViewModelState,
    content: @Composable (NavHostController) -> Unit,
) {
    val controller = rememberNavController()
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = controller,
        startDestination = "wallet"
    ) {
        composable("wallet") { updatedContent(controller) }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            ProfileScreen(
                principal = id,
                userId = backStackEntry.arguments?.getString("id") ?: "",
                provider = provider,
                viewModelStore = viewModelStore,
            )
        }
    }
}
