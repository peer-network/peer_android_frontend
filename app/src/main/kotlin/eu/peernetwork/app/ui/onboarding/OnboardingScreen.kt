package eu.peernetwork.app.ui.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.app.model.Properties
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.compose.DesignScene
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.user.domain.model.Preference
import kotlinx.collections.immutable.persistentListOf

@Composable
fun OnboardingScreen(
    preference: Preference,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onFinished: (Preference) -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Onboarding.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = OnboardingViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val isFinished = remember {
        derivedStateOf { status is OnboardingViewModel.Status.Success }
    }
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                OnboardingViewModel.State.Default -> DesignSceneState.Default
                OnboardingViewModel.State.Loading -> DesignSceneState.Loading
                is OnboardingViewModel.State.Success -> {
                    val data = (state as OnboardingViewModel.State.Success)
                    DesignSceneState.Success(data)
                }
                is OnboardingViewModel.State.Error -> {
                    DesignSceneState.Error((state as OnboardingViewModel.State.Error).error)
                }
            }
        }
    }
    val handleOnFinished by rememberUpdatedState(onFinished)
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 5 })
    DesignScene(
        state = derivedState,
        modifier = Modifier.fillMaxSize(),
        error = { DesignError(
            onRefresh = { viewModel.initialize() },
            error = it.value,
            resource = component.resource()) }
    ) { state -> OnboardingScreen(pagerState, properties = state.value.properties) {
        viewModel.finish(it, preference.copy(
            flags = state.value
                .properties
                .configuration
                .onboarding
                .availableOnboardings
        ))
    } }
    LaunchedEffect(isFinished.value) {
        if (isFinished.value) {
            (status as? OnboardingViewModel.Status.Success?)?.preference?.let { pref ->
                handleOnFinished(pref)
                viewModel.reset()
            }
        }
    }
    LaunchedEffect(Unit) { viewModel.initialize() }
}

@Composable
fun OnboardingScreen(
    state: PagerState,
    properties: Properties,
    onFinished: (Boolean) -> Unit
) {
    val price = properties.configuration.minting.dailyNumberToken.toString()
    val states = persistentListOf(
        OnboardingGuideState.Introduction,
        OnboardingGuideState.Action,
        OnboardingGuideState.Diagram(price),
        OnboardingGuideState.Engagement(price),
        OnboardingGuideState.Feature
    )
    OnboardingScaffold(state) {
        OnboardingGuide(
            state = states[it],
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}
