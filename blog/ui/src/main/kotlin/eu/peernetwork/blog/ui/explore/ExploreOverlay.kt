package eu.peernetwork.blog.ui.explore

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.content.overlay.OverlayScreen
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ExploreOverlay(
    author: String,
    limit: Int,
    position: Int,
    enabled: Boolean,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    event: UiPostListener,
    header: @Composable () -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Explore.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ExploreViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                ExploreViewModel.State.Empty -> DesignSceneState.Default
                ExploreViewModel.State.Loading -> DesignSceneState.Loading
                is ExploreViewModel.State.Success -> {
                    DesignSceneState.Success(
                        (state as ExploreViewModel.State.Success).content
                    )
                }
                is ExploreViewModel.State.Error -> DesignSceneState.Error(
                    (state as ExploreViewModel.State.Error).error
                )
            }
        }
    }
    OverlayScreen(
        id = author,
        limit = limit,
        position = position,
        status = enabled,
        event = event,
        state = derivedState,
        provider = component,
        header = header,
        connection = connection,
        viewModelStoreOwner = viewModelStoreOwner,
        onRefresh = { viewModel.get(Pageable(0, limit)) },
        onView = { viewModel.view(it) }
    )
}
