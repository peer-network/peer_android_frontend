package eu.peernetwork.app.ui.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch

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
    val isFinished = remember { derivedStateOf {
        status is OnboardingViewModel.Status.Success
    } }
    val derivedState = remember { derivedStateOf {
        when(state) {
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
    } }
    val handleOnFinished by rememberUpdatedState(onFinished)
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 5 })
    DesignScene(
        state = derivedState,
        modifier = Modifier.fillMaxSize(),
        error = { DesignError(
            onRefresh = { viewModel.initialize() },
            error = it.value,
            resource = component.resource()) }
    ) { OnboardingScreen(pagerState, properties = it.value.properties) {
        viewModel.finish(preference.copy(
            flags = it.value
                .properties
                .configuration
                .onboarding
                .availableOnboardings
        ))
    } }
    LaunchedEffect(isFinished.value) {
        if (isFinished.value) {
            (status as? OnboardingViewModel.Status.Success?)?.preference?.let {
                handleOnFinished(it)
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
    onFinished: () -> Unit
) {
    val scope = rememberCoroutineScope()
    fun goNext() = scope.launch {
        state.animateScrollToPage((state.currentPage + 1)
            .coerceAtMost(state.pageCount - 1))
    }
    fun goBack() = scope.launch {
        state.animateScrollToPage((state.currentPage - 1)
            .coerceAtLeast(0))
    }
    HorizontalPager(
        state = state,
        userScrollEnabled = true
    ) { page ->
        when (page) {
            0 -> OnboardingPageOne(
                onSkip = onFinished,
                onNext = { goNext() }
            )
            1 -> OnboardingPageTwo(
                onSkip = onFinished,
                onBack = { goBack() },
                onNext = { goNext() }
            )
            2 -> OnboardingPageThree(
                onSkip = onFinished,
                onBack = { goBack() },
                onNext = { goNext() }
            )
            3 -> OnboardingPageFour(
                onSkip = onFinished,
                onBack = { goBack() },
                onNext = { goNext() }
            )
            4 -> OnboardingPageFive(onSkip = onFinished)
        }
    }
}
