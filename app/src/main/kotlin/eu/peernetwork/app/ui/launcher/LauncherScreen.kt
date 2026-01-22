package eu.peernetwork.app.ui.launcher

import android.content.Intent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.ui.home.HomeScreen
import eu.peernetwork.app.ui.welcome.WelcomeScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignNavigation
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.route
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.app.ui.onboarding.OnboardingScreen

@Composable
fun LauncherScreen(
    id: String?,
    route: String?,
    token: State<String?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val component = remember {
        provider.builder(Launcher.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = LauncherViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val controller = rememberNavController()
    DesignNavigation(
        navController = controller,
        startDestination = "launcher"
    ) {
        composable("launcher") {  }
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
        composable(
            route = "onboarding?initialized={initialized}",
            exitTransition = {
                fadeOut(animationSpec = tween(200)) +
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(300),
                            targetOffset = { -it / 4 }
                        )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(200)) +
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(300),
                            initialOffset = { it / 4 }
                        )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(200)) +
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(300),
                            targetOffset = { -it / 4 }
                        )
            }
        ) { backStackEntry ->
            val initialized = backStackEntry.arguments?.getString("initialized") == "true"
            OnboardingScreen(
                initialized = initialized,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
            ) {
                if (it) {
                    controller.route("home?isOnboarded=true")
                } else {
                    controller.popBackStack()
                }
            }
        }
        composable(
            route = "home?isOnboarded={isOnboarded}",
            exitTransition = {
                fadeOut(animationSpec = tween(200)) +
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(300),
                            targetOffset = { -it / 4 }
                        )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(200)) +
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(300),
                            initialOffset = { it / 4 }
                        )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(200)) +
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(300),
                            targetOffset = { -it / 4 }
                        )
            }
        ) { backStackEntry ->
            val initialized = backStackEntry.arguments?.getString("isOnboarded") == "true"
            HomeScreen(
                isOnboarded = initialized,
                provider = component,
                viewModelStoreOwner = backStackEntry
            ) {
                if (it) {
                    controller.route("onboarding?initialized=false")
                } else {
                    controller.navigate("onboarding?initialized=true")
                }
            }
        }
        composable("post/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            HomeScreen(
                route = "post/$id",
                isOnboarded = false,
                provider = component,
                viewModelStoreOwner = backStackEntry
            ) {
                if (it) {
                    controller.route("onboarding?initialized=false")
                } else {
                    controller.navigate("onboarding?initialized=true")
                }
            }
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
            viewModel.reset()
            controller.route("welcome")
        }
    }
}
