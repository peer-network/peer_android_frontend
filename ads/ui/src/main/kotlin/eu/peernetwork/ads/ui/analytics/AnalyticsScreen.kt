package eu.peernetwork.ads.ui.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.luna.DesignStyledText
import eu.peernetwork.core.ui.extension.builder

@Composable
fun AnalyticsScreen(
    id: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (DesignStyledText) -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Analytics.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = AnalyticsViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                is AnalyticsViewModel.State.Default -> DesignStreamState.Default
                is AnalyticsViewModel.State.Loading -> DesignStreamState.Loading
                is AnalyticsViewModel.State.Success -> DesignStreamState.Success(
                    (state as AnalyticsViewModel.State.Success)
                )
                is AnalyticsViewModel.State.Error -> DesignStreamState.Error(
                    (state as AnalyticsViewModel.State.Error).error
                )
            }
        }
    }
    DesignStream(
        state = derivedState,
        loading = { AnalyticsSkeleton() },
        error = {
            AnalyticsError(
                error = it,
                component = component,
                onRefresh = { viewModel(id) }
            )
        }
    ) { target ->
        val isRefreshing = remember { mutableStateOf(false) }
        DesignRefreshScaffold(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel(id) }
        ) {
            AnalyticsPage(
                title = target.value.campaign.ads.content.title,
                description = target.value.campaign.ads.content.description,
                status = target.value.campaign.ads.status,
                metrics = target.value.campaign.metrics,
                start = target.value.campaign.ads.from,
                end = target.value.campaign.ads.to,
                onClick = onClick
            ) {
                AnalyticsMedia(
                    url = target.value.campaign.ads.content.path,
                    component = component
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        if (state is AnalyticsViewModel.State.Default) {
            viewModel(id)
        }
    }
}
