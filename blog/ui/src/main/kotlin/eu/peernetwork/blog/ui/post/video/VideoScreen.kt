package eu.peernetwork.blog.ui.post.video

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import eu.peernetwork.blog.ui.comment.CommentSheet
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.compose.MediaPostCard
import eu.peernetwork.blog.ui.compose.PostSummary
import eu.peernetwork.blog.ui.compose.PostPageSkeleton
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.mapper.mapToEngagement
import eu.peernetwork.blog.ui.post.photo.formatTimeAgo
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignPagingContent
import eu.peernetwork.core.ui.design.component.DesignStatefulContentState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.VideoThumbnail
import kotlinx.coroutines.delay

@Composable
fun VideoScreen(
    author: String,
    postLimit: Int,
    loadState: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
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
    val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val derivedState = remember { derivedStateOf {
        when(state) {
            VideoViewModel.State.Empty -> DesignStatefulContentState.Empty
            VideoViewModel.State.Loading -> DesignStatefulContentState.Loading
            is VideoViewModel.State.Success -> {
                DesignStatefulContentState.Success(
                    (state as VideoViewModel.State.Success).content
                )
            }
            is VideoViewModel.State.Error -> {
                DesignStatefulContentState.Error((state as VideoViewModel.State.Error).error)
            }
        }
    } }
    var selectedClip = remember { mutableStateOf<Int?>(null) }
    var selectedPost = remember { mutableStateOf<String?>(null) }
    DesignPagingContent<UiVideo>(
        state = derivedState,
        placeholder = { PostPageSkeleton() },
        onRefresh = { viewModel.load(author, Pageable(0, postLimit)) }
    ) { contentState, lazyPagingItems ->
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
                            EngagementScreen(post.mapToEngagement(), component, viewModelStoreOwner) {
                                selectedPost.value = post.id
                            }
                        }
                    ) {
                        Box(modifier = Modifier.clickable(
                            role = Role.Button,
                            onClick = { selectedClip.value = index }
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
        LaunchedEffect(loadState.value) {
            if (loadState.value) {
                lazyPagingItems.refresh()
                loadState.value = false
            }
        }
        VideoDialog(author, postLimit, selectedClip, provider, viewModelStoreOwner)
        CommentSheet(selectedPost, postLimit, component, viewModelStoreOwner)
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTime.longValue = System.currentTimeMillis()
        }
    }
}
