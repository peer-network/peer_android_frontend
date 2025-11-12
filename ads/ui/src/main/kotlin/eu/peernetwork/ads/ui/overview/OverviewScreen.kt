package eu.peernetwork.ads.ui.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    DesignStream(state = derivedState) {
        Box(modifier = Modifier.height(100.dp)) {
            Text(it.value.toString())
        }
    }
    LaunchedEffect(Unit) {
        if (state is OverviewViewModel.State.Default) {
            viewModel(id)
        }
    }
}
