package eu.peernetwork.ads.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.ads.ui.adverts.AdvertsScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStyledText
import eu.peernetwork.core.ui.extension.builder

@Composable
fun DashboardScreen(
    id: String,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onBack: () -> Unit,
    onClick: (DesignStyledText) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Dashboard.Builder::class.java).build(context)
    }
    val controller = rememberNavController()
    DashboardNavigation(
        component = component,
        navController = controller,
        onClick = onClick
    ) {
        DashboardScreen(
            id = id,
            limit = limit,
            navController = controller,
            component = component,
            viewModelStoreOwner = viewModelStoreOwner,
            onBack = onBack,
            onClick = onClick
        )
    }
}

@Composable
fun DashboardScreen(
    id: String,
    limit: Int,
    navController: NavHostController,
    component: Dashboard.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    onBack: () -> Unit,
    onClick: (DesignStyledText) -> Unit
) {
    AdvertsScreen(
        id = id,
        limit = limit,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        onSelect = { navController.navigate("analytics/$it") },
        onBack = onBack,
        onClick = onClick
    )
}
