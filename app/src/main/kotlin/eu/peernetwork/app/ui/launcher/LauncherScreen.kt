package eu.peernetwork.app.ui.launcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.ui.home.HomeScreen
import eu.peernetwork.app.ui.setup.SetupScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignNavigation
import eu.peernetwork.core.ui.extension.attachIfNecessary
import eu.peernetwork.core.ui.extension.builder

@Composable
fun LauncherScreen(
    id: String?,
    route: String?,
    token: State<String?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Launcher.Builder::class.java).build(context)
    }
    val controller = rememberNavController()
    DesignNavigation(navController = controller, startDestination = "launcher") {
        composable("launcher") {  }
        composable("setup") {
            SetupScreen(
                referral = id,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            )
        }
        composable("home") { HomeScreen(component) }
    }
    LaunchedEffect(token) {
        if (token.value != null) {
            controller.attachIfNecessary("home")
        } else {
            controller.attachIfNecessary("setup")
        }
    }
}
