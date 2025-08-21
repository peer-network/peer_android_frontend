package eu.peernetwork.blog.ui.timeline.video

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import eu.peernetwork.blog.ui.compose.ListPreview
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
    id: String,
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
    onAuthorClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    ListPreview(
        listState = listState,
        onClear = { viewModel.reset() },
        onFocus = { position ->
            lazyPagingItems[position]?.let {
                if (!it.isViewed) {
                    viewModel.view(it.id)
                }
            }
        }
    ) { position ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            flingBehavior = ScrollableDefaults.flingBehavior(),
        ) {
            items(
                count = lazyPagingItems.itemCount,
                key = { index -> lazyPagingItems[index]?.id?.let { "$it;$index" } ?: index }
            ) { index ->
                val enable = remember { derivedStateOf { !listState.isScrollInProgress } }
                val isPlaying = remember { derivedStateOf { index == position.value && state.value } }
                lazyPagingItems[index]?.let { post ->
                    val postThumbnail = remember { derivedStateOf { thumbnail.value[post.media] } }
                    VideoListing(
                        id = id,
                        post = post,
                        index = index,
                        uiEngagementEvent = engagement,
                        uiModerationEvent = moderation,
                        onAuthorClick = onAuthorClick,
                        onPostClick = onPostClick,
                        onMentionClick = onMentionClick,
                        onHashtagClick = onHashtagClick,
                        connection = connection
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
            item(key = id) {
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
    id: String,
    post: UiVideo,
    index: Int,
    uiEngagementEvent: UiEngagementEvent,
    uiModerationEvent: UiModerationEvent,
    onAuthorClick: (String) -> Unit = {},
    onPostClick: (String, Int) -> Unit,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {},
    content: @Composable (UiVideo) -> Unit = {}
) {
    val clickHandler by rememberUpdatedState(onAuthorClick)
    val selectHandler by rememberUpdatedState { onPostClick(post.id, index) }
    val updatedContent by rememberUpdatedState(content)
    val updatedConnection by rememberUpdatedState(connection)
    val uiContent = post.mapToContent()
    MediaView(
        author = post.author,
        onAuthorClick = { clickHandler(post.author.id) },
        description = post.time,
        caption = {
            TextView(
                uiContent.author.username,
                uiContent.title,
                uiContent.description,
                onAuthorClick = { clickHandler(post.author.id) },
                onMentionClick = onMentionClick,
                onHashtagClick = onHashtagClick
            )
        },
        engagements = {
            EngagementScreen(
                event = uiEngagementEvent,
                model = uiContent,
                padding = PaddingValues(end = 4.dp),
            )
        },
        moderation = {
            ModerationScreen(
                uiContent,
                uiModerationEvent
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
            onClick = { selectHandler() }
        )) { updatedContent(post) }
    }
}
