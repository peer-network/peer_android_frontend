package eu.peernetwork.blog.ui.timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.extension.builder
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.ui.advert.AdvertScreen
import eu.peernetwork.blog.ui.advert.advert
import eu.peernetwork.blog.ui.compose.PostPlaceholder
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.blog.ui.engagement.v2.EngagementOption
import eu.peernetwork.blog.ui.mapper.mapToDetail
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.post.PostMedia
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignLoader
import kotlinx.coroutines.flow.Flow

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun TimelineScreen(
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
    onExplore: (() -> Unit)? = null,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val component = remember {
        provider.builder(Timeline.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = TimelineViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val updatedConnection by rememberUpdatedState(connection)
    val errorMessage = stringResource(R.string.unknown_error_message)
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                TimelineViewModel.State.Empty -> DesignStreamState.Default
                TimelineViewModel.State.Loading -> DesignStreamState.Loading
                is TimelineViewModel.State.Success -> DesignStreamState.Success(
                    (state as TimelineViewModel.State.Success).content
                )
                is TimelineViewModel.State.Error -> DesignStreamState.Error(
                    (state as TimelineViewModel.State.Error).error.let {
                        Throwable(component.resource()
                            .string(it.message ?: errorMessage), it)
                    }
                )
            }
        }
    }
    val pause = remember { mutableStateOf(false) }
    val enable = remember { derivedStateOf { !listState.isScrollInProgress } }
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
    AdvertScreen(
        id = id,
        postLimit = postLimit,
        listState = listState,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = connection
    ) { postComponent, event, position, ads ->
        val content = remember { derivedStateOf {
            (ads.value as? DesignStreamState.Success?)?.data
        } }
        val adverts = content.value?.collectAsLazyPagingItems()
        TimelineScreen(
            author = id,
            state = derivedState,
            listState = listState,
            header = { adverts?.let { ads ->
                advert(
                    state = ads,
                    engagement = { post ->
                        EngagementOption(
                            post = post,
                            state = event.observe()
                        ) { event(post, it) }
                    },
                    connection = {
                        updatedConnection(
                            Triple(
                                it.author.id,
                                it.author.isfollowing,
                                it.author.isfollowed
                            )
                        )
                    }
                ) { post, path, index ->
                    val isActive = remember { derivedStateOf { index == position.value } }
                    PostMedia(
                        type = post.type,
                        path = path,
                        avatar = post.author.imageUrl,
                        position = index,
                        aspectRatio = post.aspectRatio,
                        status = status,
                        enable = enable,
                        isActive = isActive,
                        component = postComponent,
                        viewModelStoreOwner = viewModelStoreOwner,
                    )
                }
            } },
            connection = {
                updatedConnection(
                    Triple(
                        it.author.id,
                        it.author.isfollowing,
                        it.author.isfollowed
                    )
                )
            },
            engagement = { post ->
                EngagementOption(
                    post = post,
                    state = event.observe()
                ) { event(post, it) }
            }
        ) { post, path, index ->
            val isActive = remember { derivedStateOf { index == position.value } }
            PostMedia(
                type = post.type,
                path = path,
                avatar = post.author.imageUrl,
                position = index,
                aspectRatio = post.aspectRatio,
                status = status,
                enable = enable,
                isActive = isActive,
                component = postComponent,
                viewModelStoreOwner = viewModelStoreOwner,
            )
        }
    }
    LaunchedEffect(category, criteria) {
        val currentState = state as? TimelineViewModel.State.Success?
        val requiresChange = currentState?.category != category
                || currentState.criteria != criteria
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

@Composable
fun TimelineScreen(
    author: String,
    state: State<DesignStreamState<Flow<PagingData<UiPost>>>>,
    listState: LazyListState,
    header: LazyListScope.() -> Unit,
    engagement: @Composable (UiPost) -> Unit,
    connection: @Composable RowScope.(UiPost) -> Unit,
    content: @Composable (UiPost, String, Int) -> Unit,
) {
    val updatedHeader by rememberUpdatedState(header)
    val updatedEngagement by rememberUpdatedState(engagement)
    val updatedConnection by rememberUpdatedState(connection)
    val updatedContent by rememberUpdatedState(content)
    DesignStream(state) { result ->
        val lazyPagingItems = result.value.collectAsLazyPagingItems()
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            updatedHeader()
            items(
                count = lazyPagingItems.itemCount,
                key = { index -> lazyPagingItems[index]?.id?.let { "$it;$index" } ?: index }
            ) { index ->
                lazyPagingItems[index]?.let { post ->
                    PostScreen(
                        type = post.type,
                        pinnedBy = null,
                        model = post.mapToDetail(),
                        media = post.media,
                        engagement = { updatedEngagement(post) },
                        connection = { updatedConnection(post) },
                        content = { path -> updatedContent(post, path, index) }
                    )
                }
            }
            if (lazyPagingItems.loadState.refresh !is LoadState.Loading) {
                item(key = author) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (lazyPagingItems.loadState.append is LoadState.Loading) {
                            DesignLoader {
                                PostPlaceholder(
                                    contentPaddingValues = PaddingValues(16.dp)
                                )
                            }
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp))
                    }
                }
            }
        }
    }
}
