package eu.peernetwork.ads.ui.analytics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.ads.ui.overview.OverviewScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignRefreshScaffold
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.mapper.annotate

@Composable
fun AnalyticsScreen(
    id: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
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
                title = target.value.ads.content.title.annotate(),
                description = target.value.ads.content.description.annotate(),
                status = target.value.ads.status,
                media = {
                    AnalyticsMedia(
                        url = target.value.ads.content.path,
                        component = component
                    )
                }
            ) {
                OverviewScreen(
                    id = id,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    modifier = Modifier.fillMaxSize()
                        .padding(bottom = 12.dp)
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
