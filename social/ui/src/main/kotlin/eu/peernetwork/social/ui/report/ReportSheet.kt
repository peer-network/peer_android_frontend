package eu.peernetwork.social.ui.report

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold

@Composable
fun ReportSheet(
    state: MutableState<String?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    val streamState = remember { derivedStateOf {
        if (state.value == null) {
            DesignStreamState.Default
        } else {
            DesignStreamState.Success(state.value!!)
        }
    } }
    ReportScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        DesignStream(streamState) { post ->
            DesignBottomSheetScaffold(
                state = showSheet,
                onDismiss = { state.value = null }
            ) {
                ReportScreen(viewModel) { stream ->
                    ReportPage(
                        onCancel = { state.value = null },
                        modifier = Modifier.padding(horizontal = 20.dp)
                            .padding(top = 20.dp)
                            .padding(bottom = 8.dp)
                            .navigationBarsPadding()
                    ) { viewModel(post.value) }
                }
            }
        }
    }
}
