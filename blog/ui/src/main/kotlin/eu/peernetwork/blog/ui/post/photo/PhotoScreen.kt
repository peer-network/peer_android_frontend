package eu.peernetwork.blog.ui.post.photo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.compose.PostListItem
import eu.peernetwork.blog.ui.compose.PostPageSkeleton
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.engagement.EngagementSpec
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.mapper.mapToProperty
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.moderation.ModerationSpec
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.ImageView
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoScreen(
    author: String,
    postLimit: Int,
    loadState: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    imageOnClick: (String) -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Photo.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = PhotoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                PhotoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                PhotoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is PhotoViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as PhotoViewModel.State.Success).content
                    )
                }
                is PhotoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as PhotoViewModel.State.Error).error
                )
            }
        }
    }
    DesignPagingScaffold<UiPost>(
        state = derivedState,
        placeholder = { PostPageSkeleton() },
        onRefresh = { viewModel.load(author, Pageable(0, postLimit)) },
    ) { state, lazyPagingItems ->
        val refreshed = remember { derivedStateOf {
            lazyPagingItems.loadState.refresh is LoadState.NotLoading
        } }
        EngagementScreen(
            postLimit,
            refreshed,
            onMentionClick = onMentionClick,
            onHashtagClick = onHashtagClick,
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
                        lazyPagingItems[index]?.let { photo ->
                            PhotoScreen(
                                post = photo,
                                index = index,
                                currentTime = currentTime,
                                onMentionClick = onMentionClick,
                                onHashtagClick = onHashtagClick,
                                engagementSpec = engagement,
                                moderationSpec = spec
                            ) {
                                val media = photo.media.first()
                                component.imageView()(
                                    Modifier,
                                    ImageView.Spec(media.path, media.mapToProperty())
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
        LaunchedEffect(loadState.value) {
            if (loadState.value) {
                lazyPagingItems.refresh()
                loadState.value = false
            }
        }
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTime.longValue = System.currentTimeMillis()
        }
    }
}

fun Long.formatTimeAgo(time: Long): String {
    val diff = time - this
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "$minutes minutes ago"
        hours < 24 -> "$hours hours ago"
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
fun LazyItemScope.PhotoScreen(
    post: UiPost,
    index: Int,
    currentTime: State<Long>,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    engagementSpec: EngagementSpec,
    moderationSpec: ModerationSpec,
    content: @Composable (UiPost) -> Unit = {}
) {
    val model = remember(post) { post.mapToContent() }
    PostListItem(
        post,
        index,
        currentTime,
        onMentionClick = onMentionClick,
        onHashtagClick = onHashtagClick,
        engagements = { EngagementScreen(
            model,
            engagementSpec
        ) },
        moderation = {
            ModerationScreen(
                model = model,
                spec = moderationSpec
            )
        },
        content = content
    )
}
