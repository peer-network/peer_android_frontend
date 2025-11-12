package eu.peernetwork.ads.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.ads.ui.adverts.AdvertsScreen
import eu.peernetwork.ads.ui.overview.OverviewScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.extension.builder

@Composable
fun DashboardScreen(
    id: String,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Dashboard.Builder::class.java).build(context)
    }
    val controller = rememberNavController()
    DashboardNavigation(component, controller) {
        DashboardScreen(
            id = id,
            limit = limit,
            navController = controller,
            component = component,
            viewModelStoreOwner = viewModelStoreOwner
        )
    }
}

@Composable
fun DashboardScreen(
    id: String,
    limit: Int,
    navController: NavHostController,
    component: Dashboard.Component,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    DesignScaffold(
        alwaysReturn = true,
        modifier = Modifier.fillMaxSize(),
        header = {
            OverviewScreen(
                id = id,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()) {
            AdvertsScreen(
                limit = limit,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            ) { navController.navigate("analytics") }
        }
    }
}
