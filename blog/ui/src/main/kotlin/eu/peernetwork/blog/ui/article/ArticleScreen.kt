package eu.peernetwork.blog.ui.article

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.ui.compose.PostPlaceholder
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.mapper.mapToDetail
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.post.PostMedia
import eu.peernetwork.blog.ui.engagement.v2.EngagementOption
import eu.peernetwork.blog.ui.post.PostScreen
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignLoader
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.flow.Flow

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ArticleScreen(
    author: String,
    types: Set<Content.Type>,
    postLimit: Int,
    status: State<Boolean>,
    requireUpdate: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    event: UiPostListener,
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val component = remember { provider.builder(Article.Builder::class.java).build(context) }
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
    val updatedConnection by rememberUpdatedState(connection)
    PostScreen(
        id = author,
        limit = postLimit,
        listState = listState,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = connection
    ) { postComponent, event, position ->
        ArticleScreen(
            author = author,
            state = derivedState,
            listState = listState,
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
    LaunchedEffect(requireUpdate.value) {
        if (requireUpdate.value || localState.value is ArticleViewModel.State.Empty) {
            viewModel.load(
                author,
                types,
                Pageable(0, postLimit)
            )
            requireUpdate.value = false
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
fun ArticleScreen(
    author: String,
    state: State<DesignStreamState<Flow<PagingData<UiPost>>>>,
    listState: LazyListState,
    engagement: @Composable (UiPost) -> Unit,
    connection: @Composable RowScope.(UiPost) -> Unit,
    content: @Composable (UiPost, String, Int) -> Unit,
) {
    val updatedEngagement by rememberUpdatedState(engagement)
    val updatedConnection by rememberUpdatedState(connection)
    val updatedContent by rememberUpdatedState(content)
    DesignStream(state) { result ->
        val lazyPagingItems = result.value.collectAsLazyPagingItems()
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
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
