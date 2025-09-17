package eu.peernetwork.blog.ui.feed.timeline

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
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.blog.ui.content.timeline.TimelineScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import eu.peernetwork.core.ui.extension.attachIfNecessary

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun PostScreen(
    id: String,
    status: State<Boolean>,
    postLimit: Int,
    category: Category,
    event: UiPostListener,
    criteria: Criteria? = null,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    requireUpdate: MutableState<Boolean>,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
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
    PostNavigation(
        explore = { navController: NavController ->
            TimelineScreen(
                id = id,
                limit = postLimit,
                event = event,
                state = derivedState,
                provider = component,
                connection = connection,
                listState = listState,
                viewModelStoreOwner = viewModelStoreOwner,
                onRefresh = { viewModel.load(Pageable(0, postLimit), Category.NONE, criteria) },
                onLoad = {
                    if (requireUpdate.value) {
                        it.value.refresh()
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                        requireUpdate.value = false
                    }
                },
                onView = { viewModel.view(it) },
                status = status
            )
        },
        content = { navController ->
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
                onLoad = {
                    if (requireUpdate.value) {
                        it.value.refresh()
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                        requireUpdate.value = false
                    }
                },
                onExplore = {
                    navController.attachIfNecessary("explore")
                            },
                onView = { viewModel.view(it) },
                status = status
            )
        }
    )
    LaunchedEffect(category, criteria) {
        val currentState = state as? PostViewModel.State.Success?
        val requiresChange = currentState?.category != category
                || currentState?.criteria != criteria
        if (requiresChange) {
            viewModel.load(Pageable(0, postLimit), category, criteria)
            requireUpdate.value = true
        }
    }
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}