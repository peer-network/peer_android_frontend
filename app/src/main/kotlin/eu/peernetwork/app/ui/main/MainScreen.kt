package eu.peernetwork.app.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.ui.home.HomeScreen
import eu.peernetwork.app.ui.setup.SetupScreen
import eu.peernetwork.app.ui.splash.SplashScreen
import eu.peernetwork.core.ui.extension.attachIfNecessary

@Composable
fun MainScreen(
    component: Main.Component,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val controller = rememberNavController()
    val viewModel = viewModel(
        modelClass = MainViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    var splashDone by remember { mutableStateOf(false) }
    LaunchedEffect(state, splashDone) {
        if (!splashDone) return@LaunchedEffect
        when(state) {
            is MainViewModel.State.Splash -> Unit
            is MainViewModel.State.Startup -> controller.attachIfNecessary("startup")
            is MainViewModel.State.Home -> controller.attachIfNecessary("home")
        }
    }
    NavHost(navController = controller, startDestination = "splash") {
        composable("splash") {
            SplashScreen(component, viewModelStoreOwner) { splashDone = true }
        }
        composable("startup") {
            SetupScreen(
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            )
        }
        composable("home") { HomeScreen(component) }
    }
}
