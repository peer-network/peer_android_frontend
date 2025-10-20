package eu.peernetwork.app.ui.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.core.ui.design.compose.DesignError
import eu.peernetwork.core.ui.design.material.DesignNavigation
import eu.peernetwork.core.ui.design.material.DesignScene
import eu.peernetwork.core.ui.design.material.DesignSceneState

@Composable
fun HomeScaffold(
    state: State<DesignSceneState<HomeViewModel.State.Success>>,
    navController: NavHostController,
    onRefresh: () -> Unit = {},
    resource: ResourceInteractor,
    error: @Composable (error: State<Throwable>) -> Unit = { DesignError(onRefresh, it.value, resource) },
    onboarding: @Composable (HomeViewModel.State.Success) -> Unit,
    content: @Composable (HomeViewModel.State.Success) -> Unit
) {
    val updatedOnBoarding by rememberUpdatedState(onboarding)
    val updatedContent by rememberUpdatedState(content)
    DesignScene(
        state = state,
        modifier = Modifier.fillMaxSize(),
        loading = { HomeSkeleton() },
        error = error
    ) { data ->
        val startDestination = remember { derivedStateOf {
            if (data.value.preference.flags.isEmpty()) {
                "onboarding"
            } else {
                "home"
            } } }
        DesignNavigation(
            navController = navController,
            startDestination = startDestination.value,
            enterTransition = {
                fadeIn(animationSpec = tween(200)) +
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(300),
                            initialOffset = { it / 4 }
                        )
            },
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
        ) {
            composable("home") {
                updatedContent(data.value)
            }
            composable("onboarding") {
                updatedOnBoarding(data.value)
            }
        }
    }
}
