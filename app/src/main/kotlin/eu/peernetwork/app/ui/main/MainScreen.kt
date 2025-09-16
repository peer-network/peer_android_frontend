package eu.peernetwork.app.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import eu.peernetwork.app.ui.launcher.LauncherScreen
import eu.peernetwork.app.ui.splash.SplashScreen
import eu.peernetwork.app.ui.onboarding.OnboardingScreen
import eu.peernetwork.core.ui.design.compose.DesignNavigation
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
    val derivedState = remember(state.token) { derivedStateOf { state.token?.access } }
    var playSplash by rememberSaveable { mutableStateOf(false) }

    val onboardingCompleted by viewModel.onboardingCompleted.collectAsStateWithLifecycle()

    LaunchedEffect(playSplash, onboardingCompleted) {
        if (!playSplash) return@LaunchedEffect
        val destination = if (onboardingCompleted) "launcher" else "onboarding"
        controller.attachIfNecessary(destination)
    }

    DesignNavigation(navController = controller, startDestination = "splash") {
        composable("splash") { SplashScreen(component, viewModelStoreOwner) { playSplash = true } }

        composable("onboarding") {
            OnboardingScreen(
                component,
                viewModelStoreOwner
            ) {
                controller.attachIfNecessary("launcher")
            }
        }

        composable(
            "launcher",
            deepLinks = listOf(
                navDeepLink { uriPattern = "peer://{route}" },
                navDeepLink { uriPattern = "peer://{route}/{id}" }
            )
        ) {
            LauncherScreen(
                it.arguments?.getString("id"),
                it.arguments?.getString("route"),
                derivedState,
                component,
                viewModelStoreOwner
            )
        }
    }
}