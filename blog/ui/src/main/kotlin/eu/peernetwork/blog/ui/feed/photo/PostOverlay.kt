package eu.peernetwork.blog.ui.feed.photo

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.content.overlay.OverlayScreen
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.extension.builder

@Composable
fun PostOverlay(
    id: String,
    limit: Int,
    position: Int,
    enabled: Boolean,
    category: Category,
    criteria: Criteria? = null,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    event: UiPostEvent,
    header: @Composable () -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Post.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = PostViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.unknown_error_message)
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                PostViewModel.State.Empty -> DesignSceneState.Default
                PostViewModel.State.Loading -> DesignSceneState.Loading
                is PostViewModel.State.Success -> DesignSceneState.Success(
                    (state as PostViewModel.State.Success).content
                )
                is PostViewModel.State.Error -> DesignSceneState.Error(
                    (state as PostViewModel.State.Error).error.let {
                        Throwable(component.resource()
                            .string(it.message ?: errorMessage), it)
                    }
                )
            }
        }
    }
    OverlayScreen(
        id = id,
        limit = limit,
        position = position,
        enabled = enabled,
        event = event,
        state = derivedState,
        provider = component,
        header = header,
        connection = connection,
        viewModelStoreOwner = viewModelStoreOwner,
        onRefresh = { viewModel.load(Pageable(0, limit), category, criteria) },
        onView = { viewModel.view(it) }
    )
}
