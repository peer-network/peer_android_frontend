package eu.peernetwork.blog.ui.content.timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.ui.post.PostList
import eu.peernetwork.blog.ui.compose.PhotoIndicator
import eu.peernetwork.blog.ui.compose.PhotoPager
import eu.peernetwork.blog.ui.compose.PostItem
import eu.peernetwork.blog.ui.compose.PostPlaceholder
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.event.UiModerationEvent
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.design.material.DesignLoader

@Composable
fun TimelineList(
    id: String,
    current: MutableState<Int>,
    lazyPagingItems: State<LazyPagingItems<UiPost>>,
    listState: LazyListState,
    engagement: UiEngagementEvent,
    moderation: UiModerationEvent,
    event: UiPostListener,
    onView: (String) -> Unit,
    audio: @Composable (UiPost, Int, State<Int>, Boolean) -> Unit = { path, index, position, expanded -> },
    video: @Composable (UiPost, Int, State<Int>) -> Unit = { path, index, position -> },
    image: @Composable (String, Float) -> Unit = { path, ratio -> },
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit = {}
) {
    val updatedAudio by rememberUpdatedState(audio)
    val updatedVideo by rememberUpdatedState(video)
    val updatedImage by rememberUpdatedState(image)
    val updatedConnection by rememberUpdatedState(connection)
    TimelineList(
        id = id,
        current = current,
        onView = onView,
        lazyPagingItems = lazyPagingItems,
        listState = listState,
    ) { post, index, position ->
        val uiContent by remember { derivedStateOf { post.mapToContent() } }
        PostItem(
            post = post,
            position = index,
            onClick = {
                event(UiPostListener.Event.Post(post.id, index))
            },
            onAuthorClick = {
                if (post.author.id != id) {
                    event(UiPostListener.Event.Author(post.author.id))
                }
            },
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
            audio = { post, expanded -> updatedAudio(post, index, position, expanded) },
            video = { updatedVideo(it, index, position) },
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
        )
    }
}

@Composable
fun TimelineList(
    id: String,
    current: MutableState<Int>,
    lazyPagingItems: State<LazyPagingItems<UiPost>>,
    listState: LazyListState,
    onView: (String) -> Unit,
    content: @Composable (UiPost, Int, State<Int>) -> Unit = { post, index, position -> },
) {
    val handleOnView by rememberUpdatedState(onView)
    val updatedContent by rememberUpdatedState(content)
    PostList(
        listState = listState,
        onFocused = { current.value = it },
        onFocus = { position ->
            if (position < lazyPagingItems.value.itemCount) {
                lazyPagingItems.value[position]?.let {
                    if (!it.isViewed) {
                        handleOnView(it.id)
                    }
                }
            }
        }
    ) { position ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = lazyPagingItems.value.itemCount,
                key = { index -> lazyPagingItems.value[index]?.id?.let { "$it;$index" } ?: index }
            ) { index ->
                lazyPagingItems.value[index]?.let { post ->
                    updatedContent(post, index, position)
                }
            }
            item(key = id) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (lazyPagingItems.value.loadState.append is LoadState.Loading) {
                        DesignLoader { PostPlaceholder(contentPaddingValues = PaddingValues(16.dp)) }
                    }
                    Box(modifier = Modifier.fillMaxWidth()
                        .height(48.dp))
                }
            }
        }
    }
}
