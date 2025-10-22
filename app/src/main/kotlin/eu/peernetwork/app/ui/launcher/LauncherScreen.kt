package eu.peernetwork.app.ui.launcher

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.ui.home.HomeScreen
import eu.peernetwork.app.ui.setup.SetupScreen
import eu.peernetwork.app.ui.welcome.WelcomeScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignNavigation
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.route
import androidx.core.net.toUri

@Composable
fun LauncherScreen(
    id: String?,
    route: String?,
    token: State<String?>,
    provider: UiComponentProvider
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val component = remember {
        provider.builder(Launcher.Builder::class.java).build(context)
    }
    val controller = rememberNavController()
    DesignNavigation(
        navController = controller,
        startDestination = "launcher"
    ) {
        composable("launcher") {  }
        composable("setup") { backstack ->
            val code = try {
                id ?: clipboardManager.getText()?.text
                    ?.takeIf { it.startsWith("peer://invite/") }
                    ?.substringAfter("peer://invite/")
            } catch (_: Throwable) { null }
            SetupScreen(
                referral = code,
                provider = component,
                viewModelStoreOwner = backstack
            )
        }
        composable("welcome") {
            val code = try {
                id ?: clipboardManager.getText()?.text
                    ?.takeIf { it.startsWith("peer://invite/") }
                    ?.substringAfter("peer://invite/")
            } catch (_: Throwable) { null }
            WelcomeScreen(
                referral = code,
                provider = component,
                onBrowse = { url ->
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(intent)
                }
            )
        }
        composable("home") { backStackEntry ->
            HomeScreen(
                provider = component,
                viewModelStoreOwner = backStackEntry
            )
        }
        composable("post/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            HomeScreen(
                route = "post/$id",
                provider = component,
                viewModelStoreOwner = backStackEntry
            )
        }
    }
    LaunchedEffect(token.value) {
        if (token.value != null) {
            if (id != null && route != "invite") {
                try {
                    controller.route("$route/${id}")
                } catch (_: Throwable) {
                    controller.route("home")
                }
            } else {
                controller.route("home")
            }
        } else {
            controller.route("welcome")
        }
    }
}
