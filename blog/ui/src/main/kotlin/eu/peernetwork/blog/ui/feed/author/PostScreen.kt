package eu.peernetwork.blog.ui.feed.author

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.content.timeline.TimelineScreen
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.extension.builder

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PostScreen(
    author: String,
    types: Set<Content.Type>,
    postLimit: Int,
    status: State<Boolean>,
    lastUpdated: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    event: UiPostListener,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val component = remember { provider.builder(Post.Builder::class.java).build(context) }
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
    val pause = remember { mutableStateOf(false) }
    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    pause.value = false
                }
                Lifecycle.Event.ON_STOP -> {
                    pause.value = true
                }
                else -> Unit
            }
        }
    }
    TimelineScreen(
        id = author,
        limit = postLimit,
        event = event,
        state = derivedState,
        provider = component,
        connection = connection,
        listState = listState,
        viewModelStoreOwner = viewModelStoreOwner,
        onView = { viewModel.view(it) },
        enable = false,
        onRefresh = {
            viewModel.load(
                author,
                types,
                Pageable(0, postLimit),
                lastUpdated.value
            )
        },
        empty = { DesignError({
            viewModel.load(
                author,
                types,
                Pageable(0, postLimit),
                lastUpdated.value
            )
        }, NoContentException(), component.resource()) },
        status = status
    )
    LaunchedEffect(lastUpdated.value) {
        val currentState = state as? PostViewModel.State.Success?
        if (currentState?.updatedAt != lastUpdated.value) {
            viewModel.load(
                author,
                types,
                Pageable(0, postLimit),
                lastUpdated.value
            )
        }
    }
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
