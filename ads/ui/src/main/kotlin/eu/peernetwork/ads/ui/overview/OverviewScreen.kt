package eu.peernetwork.ads.ui.overview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder

@Composable
fun OverviewScreen(
    id: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Overview.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = OverviewViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            is OverviewViewModel.State.Default -> DesignStreamState.Default
            is OverviewViewModel.State.Loading -> DesignStreamState.Loading
            is OverviewViewModel.State.Success -> {
                DesignStreamState.Success(
                    (state as OverviewViewModel.State.Success).metrics
                )
            }
            is OverviewViewModel.State.Error -> {
                DesignStreamState.Error(
                    (state as OverviewViewModel.State.Error).error
                )
            }
        }
    } }
    val error = remember { derivedStateOf {
        (state as? OverviewViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    DesignStream(
        state = derivedState,
        loading = { OverviewSkeleton() },
        error = { OverviewError {
            error.value?.let { Text(it) }
        } }
    ) { OverviewPage(it.value) }
    LaunchedEffect(Unit) {
        if (state is OverviewViewModel.State.Default) {
            viewModel(id)
        }
    }
}
