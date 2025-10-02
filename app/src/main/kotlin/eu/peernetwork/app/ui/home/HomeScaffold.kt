package eu.peernetwork.app.ui.home

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
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.compose.DesignNavigation
import eu.peernetwork.core.ui.design.compose.DesignScene
import eu.peernetwork.core.ui.design.compose.DesignSceneState

@Composable
fun HomeScaffold(
    state: State<DesignSceneState<HomeViewModel.State.Success>>,
    onRefresh: () -> Unit = {},
    resource: ResourceInteractor,
    navController: NavHostController = rememberNavController(),
    error: @Composable (error: State<Throwable>) -> Unit = { DesignError(onRefresh, it.value, resource) },
    onboarding: @Composable (HomeViewModel.State.Success) -> Unit,
    content: @Composable (HomeViewModel.State.Success) -> Unit
) {
    val updatedOnBoarding by rememberUpdatedState(onboarding)
    val updatedContent by rememberUpdatedState(content)
    val data = remember { derivedStateOf { (state.value as? DesignSceneState.Success?)?.data } }
    val startDestination = remember { derivedStateOf {
        if (data.value?.preference?.flags?.isEmpty() == true) {
            "onboarding"
        } else {
            "home"
        }
    } }
    DesignNavigation(
        navController = navController,
        startDestination = startDestination.value
    ) {
        composable("onboarding") { updatedOnBoarding(data.value!!) }
        composable("home") {
            DesignScene(
                state = state,
                modifier = Modifier.fillMaxSize(),
                error = error
            ) { updatedContent(it.value) }
        }
    }
}
