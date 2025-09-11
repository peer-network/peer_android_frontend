package eu.peernetwork.blog.ui.feed.photo

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.extension.builder
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.blog.ui.content.timeline.TimelineScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignSceneState

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun PhotoScreen(
    id: String,
    status: State<Boolean>,
    postLimit: Int,
    category: Category,
    criteria: Criteria? = null,
    event: UiPostEvent,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    requireUpdate: MutableState<Boolean>,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val component = remember {
        provider.builder(Photo.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = PhotoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.unknown_error_message)
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                PhotoViewModel.State.Empty -> DesignSceneState.Default
                PhotoViewModel.State.Loading -> DesignSceneState.Loading
                is PhotoViewModel.State.Success -> DesignSceneState.Success(
                    (state as PhotoViewModel.State.Success).content
                )
                is PhotoViewModel.State.Error -> DesignSceneState.Error(
                    (state as PhotoViewModel.State.Error).error.let {
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
        id = id,
        limit = postLimit,
        event = event,
        state = derivedState,
        provider = component,
        connection = connection,
        listState = listState,
        viewModelStoreOwner = viewModelStoreOwner,
        onRefresh = { viewModel.load(Pageable(0, postLimit), category, criteria) },
        onView = { viewModel.view(it) },
        status = status
    )
    LaunchedEffect(Unit) {
        if (state is PhotoViewModel.State.Empty) {
            viewModel.load(Pageable(0, postLimit), category, criteria)
        }
    }
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
