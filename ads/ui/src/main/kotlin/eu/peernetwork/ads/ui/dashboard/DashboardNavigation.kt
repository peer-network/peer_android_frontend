package eu.peernetwork.ads.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.peernetwork.ads.ui.analytics.AnalyticsScreen
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.design.material.DesignRouter

@Composable
fun DashboardNavigation(
    component: Dashboard.Component,
    navController: NavHostController,
    onClick: (DesignRichText, String) -> Unit,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = navController,
        startDestination = "content"
    ) {
        composable("content") { updatedContent() }
        composable(
            route = "analytics/{id}",
            arguments = listOf(navArgument("id") { this.type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            AnalyticsScreen(
                id = id,
                provider = component,
                viewModelStoreOwner = backStackEntry,
                onClick = onClick
            )
        }
    }
}
