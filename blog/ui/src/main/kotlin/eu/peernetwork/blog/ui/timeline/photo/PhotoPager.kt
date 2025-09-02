package eu.peernetwork.blog.ui.timeline.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.compose.PhotoContent
import eu.peernetwork.blog.ui.compose.PhotoPager
import eu.peernetwork.blog.ui.compose.TextContent
import eu.peernetwork.blog.ui.compose.VideoContent
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.event.UiModerationEvent
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.mapper.mapToVideo
import eu.peernetwork.blog.ui.model.UiVideo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PhotoPager(
    position: Int,
    enabled: Boolean,
    engagement: UiEngagementEvent,
    moderation: UiModerationEvent,
    lazyPagingItems: State<LazyPagingItems<UiPost>>,
    event: UiPostEvent,
    progress: @Composable (MutableFloatState) -> Unit = {},
    header: @Composable () -> Unit = {},
    background: @Composable (UiVideo) -> Unit = {},
    indicator: @Composable (PagerState, ImmutableList<UiMedia>) -> Unit = { state, items -> },
    connection: @Composable RowScope.(UiPost, Triple<String, Boolean, Boolean>) -> Unit = { post, status -> },
    audio: @Composable (UiPost, PagerState, Boolean) -> Unit = { post, state, active -> },
    video: @Composable (UiVideo, Boolean, MutableFloatState) -> Unit = { post, state, progress -> },
    image: @Composable (UiPost, String) -> Unit = { post, path -> },
) {
    val updatedProgress by rememberUpdatedState(progress)
    val updatedBackground by rememberUpdatedState(background)
    val updatedIndicator by rememberUpdatedState(indicator)
    val updatedConnection by rememberUpdatedState(connection)
    val updatedAudio by rememberUpdatedState(audio)
    val updatedVideo by rememberUpdatedState(video)
    val updatedImage by rememberUpdatedState(image)
    val pagerState = rememberPagerState(
        initialPage = position
    ) { lazyPagingItems.value.itemCount }
    VerticalPager(pagerState) { page ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val post = lazyPagingItems.value[page]
            if (post != null) {
                if (post.type == UiPost.Type.TEXT) {
                    TextContent(
                        post = post,
                        onAuthorClick = event::onAuthorClick,
                        onMentionClick = event::onMentionClick,
                        onHashtagClick = event::onHashtagClick,
                        uiEngagementEvent = engagement,
                        uiModerationEvent = moderation,
                        connection = { updatedConnection(post, it) },
                        header = header
                    )
                } else if (post.type == UiPost.Type.IMAGE) {
                    val state = rememberPagerState(initialPage = 0) { post.media.size }
                    PhotoContent(
                        post = post,
                        uiEngagementEvent = engagement,
                        uiModerationEvent = moderation,
                        header = header,
                        indicator = { updatedIndicator(state, post.media.toPersistentList()) },
                        onAuthorClick = event::onAuthorClick,
                        onMentionClick = event::onMentionClick,
                        onHashtagClick = event::onHashtagClick,
                        connection = { updatedConnection(post, it) },
                    ) {
                        if (post.media.size > 1) {
                            PhotoPager(
                                pagerState,
                                0f,
                                post.media
                            ) { path -> updatedImage(post, path) }
                        } else {
                            updatedImage(post, post.media.first().path)
                        }
                    }
                } else if (post.type == UiPost.Type.AUDIO) {

                } else if (post.type == UiPost.Type.VIDEO) {
                    val videoPost = post.mapToVideo()
                    val progress = remember { mutableFloatStateOf(0f) }
                    VideoContent(
                        post = videoPost,
                        index = page,
                        uiEngagementEvent = engagement,
                        uiModerationEvent = moderation,
                        onAuthorClick = event::onAuthorClick,
                        onPostClick = event::onPostClick,
                        onMentionClick = event::onMentionClick,
                        onHashtagClick = event::onHashtagClick,
                        header = header,
                        connection = { updatedConnection(post, it) },
                        progress = { updatedProgress(progress) },
                        background = { updatedBackground(videoPost) }
                    ) { updatedVideo(videoPost, enabled && page == pagerState.currentPage, progress) }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}
