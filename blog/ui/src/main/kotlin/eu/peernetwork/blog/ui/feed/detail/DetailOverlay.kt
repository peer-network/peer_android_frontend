package eu.peernetwork.blog.ui.feed.detail

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.content.overlay.OverlayPage
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignScene
import eu.peernetwork.core.ui.design.material.DesignSceneState
import eu.peernetwork.core.ui.extension.builder

@Composable
fun DetailOverlay(
    id: String,
    userId: String,
    limit: Int,
    event: UiPostListener,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    header: @Composable () -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Detail.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = DetailViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when (state) {
            is DetailViewModel.State.Default -> {
                DesignSceneState.Default
            }
            is DetailViewModel.State.Loading -> DesignSceneState.Loading
            is DetailViewModel.State.Success -> {
                DesignSceneState.Success((state as DetailViewModel.State.Success).post)
            }
            is DetailViewModel.State.Error -> {
                DesignSceneState.Error((state as DetailViewModel.State.Error).error)
            }
        }
    } }
    DesignScene(derivedState) { post ->
        OverlayPage(
            userId = userId,
            limit = limit,
            status = true,
            event = event,
            state = post,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner,
            onView = { viewModel.view(it) },
            connection = connection,
            header = header
        )
    }
}
