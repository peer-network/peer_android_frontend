package eu.peernetwork.app.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.ui.home.HomeScreen
import eu.peernetwork.app.ui.setup.SetupScreen
import eu.peernetwork.app.ui.splash.SplashScreen
import eu.peernetwork.core.ui.extension.attach

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
    LaunchedEffect(state) {
        when(state) {
            is MainViewModel.State.Loading -> controller.attach("splash")
            is MainViewModel.State.Startup -> controller.attach("startup")
            is MainViewModel.State.Authenticated -> controller.attach("home")
        }
    }
    NavHost(navController = controller, startDestination = "splash") {
        composable("splash") { SplashScreen(component) }
        composable("startup") {
            SetupScreen(
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                isRegistration = (state as MainViewModel.State.Startup).isRegistration,
                showRegistration = { viewModel.showRegistration(it) }
            )
        }
        composable("home") { HomeScreen(component) }
    }
}
