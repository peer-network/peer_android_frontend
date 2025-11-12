package eu.peernetwork.ads.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.peernetwork.ads.ui.analytics.AnalyticsScreen
import eu.peernetwork.core.ui.design.material.DesignRouter

@Composable
fun DashboardNavigation(
    component: Dashboard.Component,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignRouter(
        navController = navController,
        startDestination = "content"
    ) {
        composable("content") { updatedContent() }
        composable("analytics") {
            AnalyticsScreen(
                provider = component,
                viewModelStoreOwner = it
            )
        }
    }
}
