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
import eu.peernetwork.blog.ui.comment.CommentBottomSheet
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.compose.MediaPostCard
import eu.peernetwork.blog.ui.compose.PostSummary
import eu.peernetwork.blog.ui.compose.PostIcon
import eu.peernetwork.blog.ui.compose.PostPageSkeleton
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.post.photo.formatTimeAgo
import eu.peernetwork.core.ui.design.component.DesignPagingContent
import eu.peernetwork.core.ui.design.component.DesignRefreshableContent
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.media.core.renderer.VideoThumbnail

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
    var position = remember { mutableStateOf<Int?>(null) }
    var selectedPostId = remember { mutableStateOf<String?>(null) }
    DesignPagingContent<UiVideo>(
        state = derivedState,
        onRefresh = { viewModel.load(Pageable(0, postLimit)) },
        placeholder = { PostPageSkeleton() }
    ) { state, lazyPagingItems ->
        val refreshState = remember { derivedStateOf {
            if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                DesignStatefulContentState.Loading
            } else if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                DesignStatefulContentState.Error(
                    (lazyPagingItems.loadState.refresh as LoadState.Error).error
                )
            } else {
                state.value
            }
        } }
        DesignRefreshableContent<LazyPagingItems<UiPost>>(
            state = refreshState,
            onRefresh = { lazyPagingItems.refresh() }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = { index -> index }
                ) { index ->
                    lazyPagingItems[index]?.let { post ->
                        MediaPostCard(
                            author = post.author,
                            description = post.createdAt.formatTimeAgo(currentTime.longValue),
                            caption = {
                                PostSummary(post.author.username, post.title, post.description)
                            },
                            engagements = {
                                PostIcon(UiAction.Like, post.likes.toString(), onClick = { })
                                PostIcon(UiAction.Dislike, post.dislikes.toString(), onClick = { })
                                PostIcon(UiAction.Comment, post.comment.toString(), onClick = { })
                            },
                            modifier = Modifier.padding(bottom = 16.dp)
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
        }
        VideoDialog(postLimit, position, provider, viewModelStoreOwner)
        CommentBottomSheet(selectedPostId)
    }
}
