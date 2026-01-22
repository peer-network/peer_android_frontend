package eu.peernetwork.app.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import eu.peernetwork.app.ui.launcher.LauncherScreen
import eu.peernetwork.app.ui.splash.SplashScreen
import eu.peernetwork.core.ui.design.material.DesignNavigation
import eu.peernetwork.core.ui.extension.attachIfNecessary

@Composable
fun MainScreen(
    component: Main.Component,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val viewModel = viewModel(
        modelClass = MainViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val controller = rememberNavController()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember(state.token) { derivedStateOf { state.token?.access } }
    DesignNavigation(
        navController = controller,
        startDestination = "splash"
    ) {
        composable(
            "splash",
            deepLinks = listOf(
                navDeepLink { uriPattern = "peer://{route}" },
                navDeepLink { uriPattern = "peer://{route}/{id}" }
            )
        ) { backstack ->
            SplashScreen(component, backstack) {
                val id = backstack.arguments?.getString("id")
                val route = backstack.arguments?.getString("route")
                controller.attachIfNecessary("launcher?route=$route&id=$id")
            }
        }
        composable("launcher?route={route}&id={id}") { backstack ->
            val id = backstack.arguments?.getString("id")?.let {
                if (it.trim().lowercase() == "null") null else it
            }
            val route = backstack.arguments?.getString("route")?.let {
                if (it.trim().lowercase() == "null") null else it
            }
            LauncherScreen(
                id = id,
                route = route,
                token = derivedState,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            )
        }
    }
}
