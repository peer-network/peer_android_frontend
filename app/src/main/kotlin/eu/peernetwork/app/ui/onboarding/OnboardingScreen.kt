package eu.peernetwork.app.ui.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import eu.peernetwork.core.ui.design.compose.DesignError
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import kotlinx.collections.immutable.persistentListOf
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OnboardingScreen(
    initialized: Boolean,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onFinished: (Boolean) -> Unit
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
    val isLoading = remember { derivedStateOf { status is OnboardingViewModel.Status.Loading } }
    val isFinished = remember {
        derivedStateOf { status is OnboardingViewModel.Status.Success }
    }
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                OnboardingViewModel.State.Default -> DesignStreamState.Default
                OnboardingViewModel.State.Loading -> DesignStreamState.Loading
                is OnboardingViewModel.State.Success -> {
                    val data = state as OnboardingViewModel.State.Success
                    DesignStreamState.Success(data)
                }
                is OnboardingViewModel.State.Error -> {
                    DesignStreamState.Error((state as OnboardingViewModel.State.Error).error)
                }
            }
        }
    }
    val handleOnFinished by rememberUpdatedState(onFinished)
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 5 })
    DesignStream(
        state = derivedState,
        modifier = Modifier.fillMaxSize(),
        error = { DesignError(
            onRefresh = { viewModel.initialize() },
            error = it.value,
            resource = component.resource()) }
    ) { state -> OnboardingScreen(pagerState, isLoading, properties = state.value.properties) {
        if (initialized) {
            handleOnFinished(false)
        } else {
            (status as? OnboardingViewModel.Status.Success?)?.preference?.let { pref ->
                handleOnFinished(false)
            } ?: viewModel.finish(it)
        }
    } }
    LaunchedEffect(isFinished.value) {
        if (isFinished.value) {
            (status as? OnboardingViewModel.Status.Success?)?.preference?.let { pref ->
                handleOnFinished(true)
                viewModel.reset()
            }
        }
    }
    LaunchedEffect(Unit) {
        if (state !is OnboardingViewModel.State.Success) {
            viewModel.initialize()
        }
    }
}

@Composable
fun OnboardingScreen(
    state: PagerState,
    isLoading: State<Boolean>,
    properties: Properties,
    onFinished: (Boolean) -> Unit
) {
    val config = properties.configuration
    val dailyNumberToken = config.minting.dailyNumberToken
    val formattedDailyMint = dailyNumberToken.formatThousands()
    val states = persistentListOf(
        OnboardingGuideState.Introduction,
        OnboardingGuideState.Action(
            dailyFreeActions = config.dailyFree.dailyFreeActions,
            actionTokenPrices = config.tokenomics.actionTokenPrices
        ),
        OnboardingGuideState.Diagram(
            dailyNumberToken = dailyNumberToken,
            formatted = formattedDailyMint
        ),
        OnboardingGuideState.Engagement(
            dailyNumberToken = dailyNumberToken,
            formatted = formattedDailyMint,
            actionGemsReturns = config.tokenomics.actionGemsReturns
        ),
        OnboardingGuideState.Feature
    )
    OnboardingScaffold(state, isLoading = isLoading, onFinish = onFinished) {
        OnboardingGuide(
            state = states[it],
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

private fun Int.formatThousands(): String {
    return NumberFormat.getIntegerInstance(Locale.getDefault()).format(this)
}