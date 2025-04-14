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
import androidx.paging.compose.collectAsLazyPagingItems
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.delay
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.paging.LoadState
import androidx.paging.PagingData
import dev.materii.pullrefresh.DragRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import eu.peernetwork.blog.ui.comment.CommentScreen
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.compose.MediaPostCard
import eu.peernetwork.blog.ui.compose.PostSummary
import eu.peernetwork.blog.ui.compose.PostIcon
import eu.peernetwork.blog.ui.engagement.EngagementsViewModel
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.blog.ui.post.photo.formatTimeAgo
import eu.peernetwork.core.ui.design.component.DesignStatefulContent
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.media.core.renderer.VideoThumbnail
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
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
    val engagementsViewModel = viewModel(
        modelClass = EngagementsViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                VideoViewModel.State.Empty -> DesignStatefulContentState.Empty
                VideoViewModel.State.Loading -> DesignStatefulContentState.Loading
                is VideoViewModel.State.Success -> DesignStatefulContentState.Success(
                    (state as VideoViewModel.State.Success).data
                )
                is VideoViewModel.State.Error -> DesignStatefulContentState.Error(
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
    var isRefreshing by remember {
        mutableStateOf(derivedState.value is DesignStatefulContentState.Loading)
    }
    var position = remember { mutableStateOf<Int?>(null) }
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        viewModel.load(Pageable(0, postLimit))
    })
    DragRefreshLayout(state = pullRefreshState) {
        DesignStatefulContent<Flow<PagingData<UiVideo>>>(
            state = derivedState,
            refresh = { viewModel.load(Pageable(0, postLimit)) }
        ) { flow ->
            val lazyPagingItems = flow.collectAsLazyPagingItems()
            var showSheet = remember { mutableStateOf(false) }
            var selectedPostId = remember { mutableStateOf("") }
            DesignBottomSheet(
                showSheet = showSheet,
                tag = "designBottomSheet",
                onDismissRequest = { showSheet.value = false },
                color = Color.White.copy(alpha = 0.9f),
                sheetPeekHeight = 600.dp,
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            CommentScreen(
                                postId = selectedPostId,
                                postLimit = postLimit,
                                provider = component,
                                viewModelStoreOwner = viewModelStoreOwner,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                    }
                }
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = { index -> index }
                ) { index ->
                    lazyPagingItems[index]?.let { post ->
                        MediaPostCard(
                            author = post.author,
                            description = post.createdAt.formatTimeAgo(currentTime.longValue),
                            modifier = Modifier.padding(bottom = 4.dp),
                            caption = {
                                PostSummary(post.author.username, post.title, post.description)
                            },
                            engagements = {
                                PostIcon(UiAction.Like, post.likes.toString(), onClick = { })
                                PostIcon(UiAction.Dislike, post.dislikes.toString(), onClick = { })
                                PostIcon(UiAction.Comment, post.comment.toString(), onClick = { })
                            }
                        ) {
                            Box(modifier = Modifier.clickable(
                                role = Role.Button,
                                onClick = { position.value = index }
                            )) {
                                component.videoThumbnail()(
                                    Modifier,
                                    VideoThumbnail.Spec(post.media, post.resolution)
                                )
                            }
                        }
                    }
                }
                if (lazyPagingItems.loadState.append is LoadState.Loading) {
                    item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                }
                item { Spacer(modifier = Modifier.height(56.dp)) }
            }
            VideoDialog(postLimit, position, provider, viewModelStoreOwner)
        }
    }
}
