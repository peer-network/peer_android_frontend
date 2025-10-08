package eu.peernetwork.app.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.compose.DesignScene
import eu.peernetwork.core.ui.design.compose.DesignSceneState

@Composable
fun HomeScaffold(
    state: State<DesignSceneState<HomeViewModel.State.Success>>,
    onRefresh: () -> Unit = {},
    resource: ResourceInteractor,
    showOnboarding: State<Boolean>,
    error: @Composable (error: State<Throwable>) -> Unit = { DesignError(onRefresh, it.value, resource) },
    onboarding: @Composable (HomeViewModel.State.Success) -> Unit,
    content: @Composable (HomeViewModel.State.Success) -> Unit
) {
    val updatedOnBoarding by rememberUpdatedState(onboarding)
    val updatedContent by rememberUpdatedState(content)
    DesignScene(
        state = state,
        modifier = Modifier.fillMaxSize(),
        error = error
    ) {
        val isOnboarding = remember { derivedStateOf {
            showOnboarding.value || it.value.preference.flags.isEmpty()
        } }
        updatedContent(it.value)
        Crossfade(isOnboarding.value) { target ->
            if (target) {
                updatedOnBoarding(it.value)
            }
        }
    }
}
