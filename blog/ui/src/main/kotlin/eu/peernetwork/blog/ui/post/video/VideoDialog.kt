package eu.peernetwork.blog.ui.post.video

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.materii.pullrefresh.DragRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
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
import eu.peernetwork.core.ui.design.compose.DesignDialogSheet
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.VideoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun VideoDialog(
    author: String,
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
    val coroutineScope = rememberCoroutineScope()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                VideoViewModel.State.Empty -> DesignStatefulContentState.Empty
                VideoViewModel.State.Loading -> DesignStatefulContentState.Loading
                is VideoViewModel.State.Success -> DesignStatefulContentState.Success(
                    (state as VideoViewModel.State.Success).content
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
        viewModel.load(author, Pageable(0, postLimit))
    })
    DesignDialogSheet(tag = "VideoDialog", visible = isVisible.value) {
        DragRefreshLayout(state = pullRefreshState) {
            DesignStatefulContent<Flow<PagingData<UiVideo>>>(
                state = derivedState,
                onRefresh = { viewModel.load(author, Pageable(0, postLimit)) }
            ) {flow ->
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
                            DialogPostCard(
                                author = post.author,
                                description = post.createdAt.formatTimeAgo(currentTime.longValue),
                                caption = {
                                    PostSummary(post.author.username, post.title, post.description)
                                },
                                engagements = {
                                    PostIcon(UiAction.Like, post.likes.toString(), onClick = {})
                                    PostIcon(UiAction.Dislike, post.dislikes.toString(), onClick = {})
                                    PostIcon(UiAction.Comment, post.dislikes.toString(), onClick = {})
                                }
                            ) {
                                Box(modifier = Modifier.weight(1f).clickable(
                                    role = Role.Button,
                                    onClick = { initialPage.value = page }
                                )) {
                                    component.videoPlayer()(
                                        Modifier.fillMaxSize(),
                                        VideoPlayer.Spec(post.media, post.resolution)
                                    )
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
    BackHandler(enabled = isVisible.value) {
        coroutineScope.launch {
            initialPage.value = null
        }
    }
}
