package eu.peernetwork.blog.ui.timeline.video

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.delay
import androidx.compose.ui.semantics.Role
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.compose.MediaPostCard
import eu.peernetwork.blog.ui.compose.PostSummary
import eu.peernetwork.blog.ui.compose.PostPageSkeleton
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.engagement.EngagementSpec
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.moderation.ModerationSpec
import eu.peernetwork.blog.ui.post.photo.formatTimeAgo
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.media.core.renderer.VideoThumbnail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    id: String,
    postLimit: Int,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Video.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = VideoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                VideoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                VideoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is VideoViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                    (state as VideoViewModel.State.Success).data
                )
                is VideoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as VideoViewModel.State.Error).error
                )
            }
        }
    }
    val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTime.longValue = System.currentTimeMillis()
        }
    }
    var selectedClip = remember { mutableStateOf<Int?>(null) }
    DesignPagingScaffold<UiVideo>(
        state = derivedState,
        onRefresh = { viewModel.load(Pageable(0, postLimit)) },
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
                id,
                postLimit,
                refreshed,
                onMentionClick,
                onHashtagClick,
                component,
                viewModelStoreOwner
            ) { engagement ->
                ModerationScreen(
                    component,
                    viewModelStoreOwner
                ) { spec ->
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = { index -> index }
                        ) { index ->
                            lazyPagingItems[index]?.let { post ->
                                VideoScreen(
                                    id = id,
                                    post = post,
                                    index = index,
                                    currentTime = currentTime,
                                    engagementSpec = engagement,
                                    moderationSpec = spec,
                                    onClick = onClick,
                                    onSelect = { selectedClip.value = it },
                                    onMentionClick = onMentionClick, onHashtagClick = onHashtagClick,
                                    connection = connection
                                ) {
                                    component.videoThumbnail()(
                                        Modifier,
                                        VideoThumbnail.Spec(post.media, post.resolution)
                                    )
                                }
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
        VideoDialog(postLimit, selectedClip, provider, viewModelStoreOwner)
    }
}

@Composable
fun VideoScreen(
    id: String,
    post: UiVideo,
    index: Int,
    currentTime: State<Long>,
    engagementSpec: EngagementSpec,
    moderationSpec: ModerationSpec,
    onClick: (String) -> Unit = {},
    onSelect: (Int) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    content: @Composable (UiVideo) -> Unit = {}
) {
    val clickHandler by rememberUpdatedState(onClick)
    val selectHandler by rememberUpdatedState { onSelect(index) }
    val updatedContent by rememberUpdatedState(content)
    val updatedConnection by rememberUpdatedState(connection)
    MediaPostCard(
        author = post.author,
        onClick = { clickHandler(post.author.id) },
        description = post.createdAt.formatTimeAgo(currentTime.value),
        caption = {
            PostSummary(
                post.author.username,
                post.title,
                post.description,
                userOnClick = { onClick(post.author.id) },
                onMentionClick = onMentionClick, onHashtagClick = onHashtagClick
            )
        },
        engagements = {
            EngagementScreen(
                spec = engagementSpec,
                model = post.mapToContent(),
            )
        },
        moderation = {
            ModerationScreen(
                post.mapToContent(),
                moderationSpec
            )
        },
        modifier = Modifier.padding(bottom = 16.dp),
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
    ) {
        Box(modifier = Modifier.clickable(
            role = Role.Button,
            onClick = selectHandler
        )) { updatedContent(post) }
    }
}
