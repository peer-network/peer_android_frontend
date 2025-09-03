package eu.peernetwork.blog.ui.post.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import eu.peernetwork.blog.ui.compose.ListView
import eu.peernetwork.blog.ui.compose.PhotoIndicator
import eu.peernetwork.blog.ui.compose.PhotoPager
import eu.peernetwork.blog.ui.compose.PostItem
import eu.peernetwork.blog.ui.compose.PostPlaceholder
import eu.peernetwork.blog.ui.event.UiEngagementEvent
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.event.UiModerationEvent
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.design.compose.DesignLoader

@Composable
fun PhotoListing(
    author: String,
    current: MutableState<Int>,
    viewModel: PhotoViewModel,
    lazyPagingItems: State<LazyPagingItems<UiPost>>,
    listState: LazyListState,
    engagement: UiEngagementEvent,
    moderation: UiModerationEvent,
    event: UiPostEvent,
    audio: @Composable (UiPost, Int, State<Int>) -> Unit = { path, index, position -> },
    video: @Composable (UiPost, Int, State<Int>) -> Unit = { path, index, position -> },
    image: @Composable (String, Float) -> Unit = { path, ratio -> }
) {
    val updatedAudio by rememberUpdatedState(audio)
    val updatedVideo by rememberUpdatedState(video)
    val updatedImage by rememberUpdatedState(image)
    PhotoListing(
        id = author,
        current = current,
        viewModel = viewModel,
        lazyPagingItems = lazyPagingItems,
        listState = listState,
    ) { post, index, position ->
        val uiContent by remember { derivedStateOf { post.mapToContent() } }
        val handleAuthorClick by rememberUpdatedState { event.onAuthorClick(post.author.id) }
        PostItem(
            post = post,
            position = index,
            onClick = { event.onPostClick(post.id, index) },
            onAuthorClick = handleAuthorClick,
            onMentionClick = event::onMentionClick,
            onHashtagClick = event::onHashtagClick,
            engagements = {
                EngagementScreen(
                    uiContent,
                    engagement,
                )
            },
            moderation = {
                ModerationScreen(
                    uiContent,
                    moderation
                )
            },
            audio = { updatedAudio(it, index, position) },
            audioPreview = { post ->
                val path = post.media.first().options.cover ?: ""
                updatedImage(path, post.aspectRatio)
            },
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
            actions = {}
        )
    }
}

@Composable
fun PhotoListing(
    id: String,
    current: MutableState<Int>,
    viewModel: PhotoViewModel,
    lazyPagingItems: State<LazyPagingItems<UiPost>>,
    listState: LazyListState,
    content: @Composable (UiPost, Int, State<Int>) -> Unit = { post, index, position -> },
) {
    val updatedContent by rememberUpdatedState(content)
    ListView(
        listState = listState,
        onFocused = { current.value = it },
        onFocus = { position ->
            if (position < lazyPagingItems.value.itemCount) {
                lazyPagingItems.value[position]?.let {
                    if (!it.isViewed) {
                        viewModel.view(it.id)
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
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (lazyPagingItems.value.loadState.append is LoadState.Loading) {
                        DesignLoader { PostPlaceholder(contentPaddingValues = PaddingValues(16.dp)) }
                    }
                }
            }
        }
    }
}
