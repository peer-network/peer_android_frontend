package eu.peernetwork.app.ui.wallet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.core.ui.design.compose.DesignNavigation

@Composable
fun WalletNavigation(content: @Composable (NavHostController) -> Unit) {
    val controller = rememberNavController()
    val updatedContent by rememberUpdatedState(content)
    DesignNavigation(
        navController = controller,
        startDestination = "wallet"
    ) {
        composable("wallet") { updatedContent(controller) }
    }
}
