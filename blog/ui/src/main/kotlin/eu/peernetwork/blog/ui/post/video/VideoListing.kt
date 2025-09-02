package eu.peernetwork.blog.ui.post.video

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.compose.MediaView
import eu.peernetwork.blog.ui.compose.TextView
import eu.peernetwork.blog.ui.compose.ListView
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.event.UiModerationEvent
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.media.core.renderer.VideoThumbnail

@Composable
fun VideoListing(
    author: String,
    state: State<Boolean>,
    component: Video.Component,
    viewModel: VideoViewModel,
    lazyPagingItems: LazyPagingItems<UiVideo>,
    listState: LazyListState,
    engagement: UiEngagementEvent,
    moderation: UiModerationEvent,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onPostClick: (String, Int) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    ListView(
        listState = listState,
        onClear = { viewModel.reset() },
        onFocus = { position ->
            if (position < lazyPagingItems.itemCount) {
                lazyPagingItems[position]?.let {
                    viewModel.view(it.id)
                }
            }
        }
    ) { position ->
        LazyColumn(state = listState) {
            items(
                count = lazyPagingItems.itemCount,
                key = { index ->
                    lazyPagingItems[index]?.id?.let {
                        "$it;$index"
                    } ?: index
                }
            ) { index ->
                val enable = remember { derivedStateOf { !listState.isScrollInProgress } }
                val isPlaying = remember { derivedStateOf { index == position.value && state.value } }
                lazyPagingItems[index]?.let { post ->
                    val postThumbnail = remember { derivedStateOf { thumbnail.value[post.media] } }
                    VideoListing(
                        post = post,
                        index = index,
                        onMentionClick = onMentionClick,
                        onHashtagClick = onHashtagClick,
                        uiEngagementEvent = engagement,
                        moderation = moderation,
                        onPostClick = onPostClick,
                    ) {
                        DesignThumbnail(
                            enable = enable,
                            thumbnail = post.media,
                            bitmap = postThumbnail,
                            modifier = Modifier.fillMaxWidth()
                                .aspectRatio(post.aspectRatio)
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            viewModel.videoBackground(
                                it,
                                post.aspectRatio,
                                configuration.screenWidthDp,
                            )
                        }
                        component.videoThumbnail()(
                            Modifier,
                            VideoThumbnail.Spec(
                                post.media,
                                post.aspectRatio,
                                isPlaying,
                                post.resolution
                            )
                        )
                    }
                }
            }
            item(key = author) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (lazyPagingItems.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun VideoListing(
    post: UiVideo,
    index: Int,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    uiEngagementEvent: UiEngagementEvent,
    moderation: UiModerationEvent,
    onPostClick: (String, Int) -> Unit,
    content: @Composable (UiVideo) -> Unit = {}
) {
    val updatedContent by rememberUpdatedState(content)
    val selectionHandler by rememberUpdatedState { onPostClick(post.id, index) }
    val uiContent = post.mapToContent()
    MediaView(
        author = post.author,
        description = post.time,
        modifier = Modifier.padding(bottom = 16.dp),
        caption = {
            TextView(
                post.author.username,
                post.title,
                post.description,
                onMentionClick = onMentionClick,
                onHashtagClick = onHashtagClick
            )
        },
        engagements = {
            EngagementScreen(
                uiContent,
                uiEngagementEvent
            )
        },
        moderation = {
            ModerationScreen(
                uiContent,
                moderation
            )
        },
    ) {
        Box(modifier = Modifier.clickable(
            role = Role.Button,
            onClick = selectionHandler
        )) { updatedContent(post) }
    }
}
