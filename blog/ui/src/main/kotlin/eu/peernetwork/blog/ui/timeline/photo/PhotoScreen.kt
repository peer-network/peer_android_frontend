package eu.peernetwork.blog.ui.timeline.photo

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.extension.builder
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.compose.PostListItem
import eu.peernetwork.blog.ui.compose.PostPageSkeleton
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.engagement.EngagementSpec
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.mapper.mapToProperty
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.moderation.ModerationSpec
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.media.core.renderer.ImageView
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PhotoScreen(
    id: String,
    postLimit: Int,
    criteria: Criteria? = null,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (String) -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Photo.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = PhotoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTime.longValue = System.currentTimeMillis()
        }
    }
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                PhotoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                PhotoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is PhotoViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as PhotoViewModel.State.Success).content
                )
                is PhotoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as PhotoViewModel.State.Error).error
                )
            }
        }
    }
    DesignPagingScaffold<UiPost>(
        state = derivedState,
        onRefresh = { viewModel.load(Pageable(0, postLimit), criteria) },
        placeholder = { PostPageSkeleton() }
    ) { state, lazyPagingItems ->
        val refreshState = remember { derivedStateOf {
            if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                DesignStatefulScaffoldState.Loading
            } else if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                DesignStatefulScaffoldState.Error(
                    (lazyPagingItems.loadState.refresh as LoadState.Error).error
                )
            } else {
                state.value
            }
        } }
        val refreshed = remember { derivedStateOf {
            lazyPagingItems.loadState.refresh is LoadState.NotLoading
        } }
        DesignRefreshableScaffold<LazyPagingItems<UiPost>>(
            state = refreshState,
            onRefresh = { lazyPagingItems.refresh() }
        ) {
            EngagementScreen(
                postLimit,
                refreshed,
                onMentionClick,
                onHashtagClick,
                onClick,
                component,
                viewModelStoreOwner
            ) { engagement ->
                ModerationScreen(
                    component,
                    viewModelStoreOwner
                ) { spec ->
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = { index -> lazyPagingItems[index]?.id ?: index }
                        ) { index ->
                            lazyPagingItems[index]?.let { post ->
                                PhotoScreen(
                                    id = id,
                                    post = post,
                                    index = index,
                                    currentTime = currentTime,
                                    engagementSpec = engagement,
                                    moderationSpec = spec,
                                    onClick = onClick,
                                    onHashtagClick = onHashtagClick,
                                    onMentionClick = onMentionClick,
                                    connection = connection,
                                    content = {
                                        val media = post.media.first()
                                        component.imageView()(
                                            Modifier,
                                            ImageView.Spec(media.path, media.mapToProperty())
                                        )
                                    }
                                )
                            }
                        }
                        if (lazyPagingItems.loadState.append is LoadState.Loading) {
                            item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                        }
                        item { Spacer(modifier = Modifier.height(56.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun LazyItemScope.PhotoScreen(
    id: String,
    post: UiPost,
    index: Int,
    currentTime: State<Long>,
    engagementSpec: EngagementSpec,
    moderationSpec: ModerationSpec,
    onClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    content: @Composable (UiPost) -> Unit = {}
) {
    val engagement = remember(post) { post.mapToContent() }
    val clickHandler by rememberUpdatedState { onClick(post.author.id) }
    val updatedConnection by rememberUpdatedState(connection)
    PostListItem(
        post = post,
        position = index,
        state = currentTime,
        onClick = clickHandler,
        userOnClick = clickHandler,
        onMentionClick = onMentionClick,
        onHashtagClick = onHashtagClick,
        engagements = {
            EngagementScreen(
                engagement,
                engagementSpec,
            ) },
        moderation = {
            ModerationScreen(
                engagement,
                moderationSpec
            )
        },
        content = content,
        actions = {
            if (id != post.author.id) {
                updatedConnection(
                    Triple(
                        post.author.id,
                        post.author.isfollowing,
                        post.author.isfollowed
                    )
                )
            }
        }
    )
}
