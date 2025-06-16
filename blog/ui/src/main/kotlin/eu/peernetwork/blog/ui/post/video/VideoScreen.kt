package eu.peernetwork.blog.ui.post.video

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.compose.MediaPostCard
import eu.peernetwork.blog.ui.compose.PostSummary
import eu.peernetwork.blog.ui.compose.PostPageSkeleton
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.engagement.EngagementSpec
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.moderation.ModerationSpec
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.VideoThumbnail
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@Composable
fun VideoScreen(
    author: String,
    postLimit: Int,
    lastUpdated: State<Long>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    imageOnClick: (String) -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
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
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                VideoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                VideoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is VideoViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as VideoViewModel.State.Success).content
                    )
                }
                is VideoViewModel.State.Error -> {
                    DesignStatefulScaffoldState.Error((state as VideoViewModel.State.Error).error)
                }
            }
        }
    }
    var selectedClip = remember { mutableStateOf<Int?>(null) }
    val updatedAt = remember { mutableLongStateOf(lastUpdated.value) }
    DesignPagingScaffold<UiVideo>(
        state = derivedState,
        placeholder = { PostPageSkeleton() },
        onRefresh = { viewModel.load(author, Pageable(0, postLimit)) },
        errorContent = { error, refresh ->
            DesignError(refresh, error, component.resource())
        }
    ) { contentState, lazyPagingItems ->
        val refreshed = remember {
            derivedStateOf {
                lazyPagingItems.loadState.refresh is LoadState.NotLoading
            }
        }
        EngagementScreen(
            postLimit,
            refreshed,
            onMentionClick,
            onHashtagClick,
            imageOnClick,
            component,
            viewModelStoreOwner
        ) { engagement ->
            ModerationScreen(
                component,
                viewModelStoreOwner
            ) { spec ->
                LazyColumn(state = listState) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = { index -> index }
                    ) { index ->
                        lazyPagingItems[index]?.let { post ->
                            VideoScreen(
                                post = post,
                                index = index,
                                currentTime = currentTime,
                                onMentionClick = onMentionClick,
                                onHashtagClick = onHashtagClick,
                                engagementSpec = engagement,
                                moderationSpec = spec,
                                onSelect = { selectedClip.value = it },
                            ) {
                                component.videoThumbnail()(
                                    Modifier,
                                    VideoThumbnail.Spec(post.media, post.resolution)
                                )
                            }
                        }
                    }
                    item(key = author) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
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
        LaunchedEffect(lastUpdated.value) {
            if (updatedAt.longValue != lastUpdated.value) {
                lazyPagingItems.refresh()
                updatedAt.longValue = lastUpdated.value
            }
        }
        VideoDialog(author, postLimit, selectedClip, provider, viewModelStoreOwner)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTime.longValue = System.currentTimeMillis()
        }
    }
}


fun Long.utcToLocalMillis(): Long {
    return this + TimeZone.getDefault().getOffset(this)
}

fun Long.formatTimeAgo(currentTimeMillis: Long): String {
    val diff = currentTimeMillis - this
    val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    return when {
        seconds < 5 -> "Just now"
        seconds < 60 -> "$seconds second${if (seconds == 1L) "" else "s"} ago"
        minutes < 60 -> "$minutes minute${if (minutes == 1L) "" else "s"} ago"
        hours < 24 -> "$hours hour${if (hours == 1L) "" else "s"} ago"
        days == 1L -> "Yesterday"
        days < 7 -> "$days days ago"
        else -> {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = this
            String.format(Locale.getDefault(), "%1\$tb %1\$td, %1\$tY", calendar)
        }
    }
}

@Composable
fun VideoScreen(
    post: UiVideo,
    index: Int,
    currentTime: State<Long>,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    engagementSpec: EngagementSpec,
    moderationSpec: ModerationSpec,
    onSelect: (Int) -> Unit,
    content: @Composable (UiVideo) -> Unit = {}
) {
    val updatedContent by rememberUpdatedState(content)
    val updatedOnSelect by rememberUpdatedState(onSelect)

    // post.createdAt to local time
    val localCreatedAtMillis = remember(post.createdAt) {
        post.createdAt.utcToLocalMillis()
    }
    //  time ago string from local time
    val timer = remember(currentTime.value) {
        localCreatedAtMillis.formatTimeAgo(currentTime.value)
    }

    MediaPostCard(
        author = post.author,
        description = timer,
        modifier = Modifier.padding(bottom = 16.dp),
        caption = {
            PostSummary(
                post.author.username,
                post.title,
                post.description,
                onMentionClick = onMentionClick,
                onHashtagClick = onHashtagClick
            )
        },
        engagements = {
            EngagementScreen(post.mapToContent(), engagementSpec)
        },
        moderation = {
            ModerationScreen(post.mapToContent(), moderationSpec)
        },
    ) {
        Box(
            modifier = Modifier.clickable(
                role = Role.Button,
                onClick = { updatedOnSelect(index) }
            )
        ) {
            updatedContent(post)
        }
    }
}
