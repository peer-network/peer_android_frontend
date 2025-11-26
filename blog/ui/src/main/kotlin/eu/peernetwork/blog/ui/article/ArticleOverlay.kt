package eu.peernetwork.blog.ui.article

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.content.overlay.OverlayScreen
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ArticleOverlay(
    author: String,
    types: Set<Content.Type>,
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
        provider.builder(Article.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ArticleViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.states.collectAsStateWithLifecycle()
    val localState = remember { derivedStateOf {
        state[types.hashCode()] ?: ArticleViewModel.State.Empty
    } }
    val errorMessage = stringResource(R.string.unknown_error_message)
    val derivedState = remember {
        derivedStateOf {
            val currentState = localState.value
            when (currentState) {
                ArticleViewModel.State.Empty -> DesignStreamState.Default
                ArticleViewModel.State.Loading -> DesignStreamState.Loading
                is ArticleViewModel.State.Success -> DesignStreamState.Success(
                    currentState.content
                )
                is ArticleViewModel.State.Error -> DesignStreamState.Error(
                    currentState.error.let {
                        Throwable(component.resource()
                            .string(it.message ?: errorMessage), it)
                    }
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
        onRefresh = { viewModel.load(author, types, Pageable(0, limit)) },
        onView = { viewModel.view(it) }
    )
    DisposableEffect(Unit) {
        onDispose {
            viewModel.selected(author, -1)
        }
    }
}
