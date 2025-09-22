package eu.peernetwork.app.ui.onboarding

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    onFinished: () -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Onboarding.Builder::class.java).build(context) }
    val vm = viewModel(
        modelClass = OnboardingViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )

    val completed by vm.completed.collectAsStateWithLifecycle()
    LaunchedEffect(completed) {
        if (completed) onFinished()
    }

    val pagesCount = 5
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pagesCount })
    val scope = rememberCoroutineScope()

    fun goNext() = scope.launch {
        pagerState.animateScrollToPage((pagerState.currentPage + 1).coerceAtMost(pagesCount - 1))
    }

    fun goBack() = scope.launch {
        pagerState.animateScrollToPage((pagerState.currentPage - 1).coerceAtLeast(0))
    }

    fun completeAndFinish() {
        vm.complete()
        onFinished()
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        userScrollEnabled = true
    ) { page ->
        when (page) {
            0 -> OnboardingPageOne(
                onSkip = { completeAndFinish() },
                onNext = { goNext() }
            )
            1 -> OnboardingPageTwo(
                onSkip = { completeAndFinish() },
                onBack = { goBack() },
                onNext = { goNext() }
            )
            2 -> OnboardingPageThree(
                onSkip = { completeAndFinish() },
                onBack = { goBack() },
                onNext = { goNext() }
            )
            3 -> OnboardingPageFour(
                onSkip = { completeAndFinish() },
                onBack = { goBack() },
                onNext = { goNext() }
            )
            4 -> OnboardingPageFive(
                onSkip = { completeAndFinish() }
            )
        }
    }
}