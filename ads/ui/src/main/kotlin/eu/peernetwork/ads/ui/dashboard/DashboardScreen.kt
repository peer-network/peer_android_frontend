package eu.peernetwork.ads.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.ads.ui.adverts.AdvertsScreen
import eu.peernetwork.ads.ui.overview.OverviewScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
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
    val isRefreshing = remember { mutableStateOf(false) }
    DesignRefreshScaffold(
        isRefreshing = isRefreshing,
        onRefresh = {}
    ) {
        DesignScaffold(
            alwaysReturn = false,
            modifier = Modifier.fillMaxSize(),
            header = {
                OverviewScreen(
                    id = id,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner
                )
            }
        ) {
            DesignScaffold(
                alwaysReturn = true,
                modifier = Modifier.fillMaxSize(),
                header = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 8.dp)
                            .padding(bottom = 10.dp)
                    ) {
                        Text(
                            "All advertisements",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            "total: 40",
                            color = MaterialTheme.colorScheme.outline,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            ) {
                AdvertsScreen(
                    limit = limit,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner
                ) { navController.navigate("analytics") }
            }
        }
    }
}
