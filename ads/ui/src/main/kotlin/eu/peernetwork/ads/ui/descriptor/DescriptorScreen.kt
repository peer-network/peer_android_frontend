package eu.peernetwork.ads.ui.descriptor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.ads.domain.model.Description
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder

@Composable
fun DescriptorScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (State<Description>) -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Descriptor.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = DescriptorViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val updatedContent by rememberUpdatedState(content)
    val errorMessage = stringResource(R.string.unknown_error_message)
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                is DescriptorViewModel.State.Default -> DesignStreamState.Default
                is DescriptorViewModel.State.Loading -> DesignStreamState.Loading
                is DescriptorViewModel.State.Success -> DesignStreamState.Success(
                    (state as DescriptorViewModel.State.Success).description
                )
                is DescriptorViewModel.State.Error -> DesignStreamState.Error(
                    (state as DescriptorViewModel.State.Error).error.let {
                        Throwable(component.resource()
                            .string(it.message ?: errorMessage), it)
                    }
                )
            }
        }
    }
    DesignStream(derivedState) { targetState -> updatedContent(targetState) }
    LaunchedEffect(Unit) {
        if (state is DescriptorViewModel.State.Default) {
            viewModel()
        }
    }
}
