package eu.peernetwork.blog.ui.content.detail

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.compose.PhotoIndicator
import eu.peernetwork.blog.ui.compose.PhotoPager
import eu.peernetwork.blog.ui.compose.PostItem
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.event.UiModerationEvent
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.design.compose.DesignScene
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun DetailPage(
    userId: String,
    state: State<DesignSceneState<UiPost>>,
    event: UiPostListener,
    engagement: UiEngagementEvent,
    moderation: UiModerationEvent,
    onLoading: (String) -> Unit = {},
    audio: @Composable (UiPost, Boolean) -> Unit = { path, expanded -> },
    video: @Composable (UiPost) -> Unit = { },
    image: @Composable (String, Float) -> Unit = { path, ratio -> },
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val updatedAudio by rememberUpdatedState(audio)
    val updatedVideo by rememberUpdatedState(video)
    val updatedImage by rememberUpdatedState(image)
    val updatedConnection by rememberUpdatedState(connection)
    val handleOnLoading by rememberUpdatedState(onLoading)
    DesignScene(state) { post ->
        val uiContent by remember { derivedStateOf { post.value.mapToContent() } }
        PostItem(
            post = post.value,
            position = 0,
            onClick = {
                event(UiPostListener.Event.Post(post.value.id, 0))
            },
            onAuthorClick = { event(UiPostListener.Event.Author(post.value.author.id)) },
            onMentionClick = { event(UiPostListener.Event.Mention(it)) },
            onHashtagClick = { event(UiPostListener.Event.Hashtag(it)) },
            engagements = {
                EngagementScreen(
                    model = uiContent,
                    event = engagement,
                    spacer = 4.dp
                )
            },
            moderation = {
                ModerationScreen(
                    model = uiContent,
                    event = moderation
                )
            },
            audio = { post, expanded -> updatedAudio(post, expanded) },
            video = { updatedVideo(it) },
            image = { post ->
                if (post.media.size == 1) {
                    val path by remember { derivedStateOf { post.media.first().path } }
                    updatedImage(path, post.aspectRatio)
                } else {
                    val pagerState = rememberPagerState(initialPage = 0) { post.media.size }
                    PhotoPager(
                        pagerState,
                        post.aspectRatio,
                        post.media,
                        { PhotoIndicator(pagerState, post.media) }
                    ) { updatedImage(it, post.aspectRatio) }
                }
            },
            actions = {
                if (userId != post.value.author.id) {
                    updatedConnection(
                        Triple(
                            post.value.author.id,
                            post.value.author.isfollowing,
                            post.value.author.isfollowed
                        )
                    )
                }
            }
        )
        LaunchedEffect(Unit) {
            snapshotFlow { state.value }
                .debounce(300)
                .collect {
                handleOnLoading(post.value.id)
            }
        }
    }
}
