package eu.peernetwork.blog.ui.timeline.video

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.materii.pullrefresh.DragRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import eu.peernetwork.blog.ui.comment.CommentScreen
import eu.peernetwork.blog.ui.compose.DialogPostCard
import eu.peernetwork.blog.ui.compose.PostIcon
import eu.peernetwork.blog.ui.compose.PostSummary
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.post.photo.formatTimeAgo
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulContent
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignDialogSheet
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.VideoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDialog(
    postLimit: Int,
    initialPage: MutableState<Int?>,
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
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isVisible = remember { derivedStateOf { initialPage.value != null } }
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
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        viewModel.load(Pageable(0, postLimit))
    })
    DesignDialogSheet(tag = "VideoDialog", visible = isVisible.value) {
        DragRefreshLayout(state = pullRefreshState) {
            DesignStatefulContent<Flow<PagingData<UiVideo>>>(state = derivedState, onRefresh = {
                viewModel.load(Pageable(0, postLimit))
            }) { flow ->
                val lazyPagingItems = flow.collectAsLazyPagingItems()
                if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    val pagerState = rememberPagerState(
                        initialPage = initialPage.value ?: 0
                    ) { lazyPagingItems.itemCount }
                    VerticalPager(pagerState) { page ->
                        val post = lazyPagingItems[page]
                        if (post != null) {
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
                            Box(modifier = Modifier.fillMaxSize()) {
                                DialogPostCard(
                                    author = post.author,
                                    description = post.createdAt.formatTimeAgo(currentTime.longValue),
                                    content = {
                                        component.videoPlayer()(
                                            Modifier.fillMaxSize(),
                                            VideoPlayer.Spec(post.media, post.resolution)
                                        )
                                    }
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(bottom = 36.dp, start = 16.dp)
                                ) {
                                    PostSummary(post.author.username, post.title, post.description, color = MaterialTheme.colorScheme.onSecondary)
                                }

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(bottom = 36.dp, end = 16.dp)) {
                                        PostIcon(UiAction.Like, post.likes.toString(), position = false, onClick = {

                                        }, color = MaterialTheme.colorScheme.onSecondary)
                                        PostIcon(UiAction.Dislike, post.dislikes.toString(), position = false, onClick = {

                                        }, color = MaterialTheme.colorScheme.onSecondary)
                                        PostIcon(UiAction.Comment, post.comment.toString(), position = false, onClick = {
                                            selectedPostId.value = post.id
                                            showSheet.value = true
                                        }, color = MaterialTheme.colorScheme.onSecondary)
                                    }
                                }
                            }
                        } else {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
