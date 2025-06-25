package eu.peernetwork.blog.ui.timeline.video

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.materii.pullrefresh.DragRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import eu.peernetwork.blog.ui.compose.AuthorView
import eu.peernetwork.blog.ui.compose.VideoProgress
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.engagement.Engagements
import eu.peernetwork.blog.ui.mapper.mapToContent
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.blog.ui.model.UiReaction
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.moderation.Moderations
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.toInt
import eu.peernetwork.media.core.renderer.VideoPlayer
import kotlinx.coroutines.flow.Flow
import androidx.media3.exoplayer.ExoPlayer
@Composable
fun VideoOverlay(
    id: String,
    limit: Int,
    position: Int,
    enabled: Boolean,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAuthorClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    connection: @Composable() (RowScope.(Triple<String, Boolean, Boolean>) -> Unit),
    engagementEvent: Engagements,
    moderationEvent: Moderations
) {

    val context = LocalContext.current
    val component = remember { provider.builder(Video.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = VideoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    val scaffoldState = remember(state) {
        derivedStateOf {
            when (state) {
                VideoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                VideoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is VideoViewModel.State.Success ->
                    DesignStatefulScaffoldState.Success((state as VideoViewModel.State.Success).data)

                is VideoViewModel.State.Error ->
                    DesignStatefulScaffoldState.Error((state as VideoViewModel.State.Error).error)
            }
        }
    }
    val updatedConnection by rememberUpdatedState(connection)

    val pullRefreshState = rememberPullRefreshState(
        refreshing = false,
        onRefresh = { viewModel.load(Pageable(0, limit)) }
    )

    DragRefreshLayout(state = pullRefreshState) {
        DesignStatefulScaffold<Flow<PagingData<UiVideo>>>(
            state = scaffoldState,
            onRefresh = { viewModel.load(Pageable(0, limit)) }
        ) { flow ->
            val items = flow.collectAsLazyPagingItems()

            if (items.loadState.refresh is LoadState.Loading) {
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            } else {
                val pagerState = rememberPagerState(
                    initialPage = position,
                    pageCount = { items.itemCount }
                )

                VerticalPager(state = pagerState) { page ->
                    val post = items[page]
                    if (post == null) {
                        Box(
                            Modifier.fillMaxSize(),
                            Alignment.Center
                        ) { CircularProgressIndicator() }
                        return@VerticalPager
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing)
                    ) {
                        var position by remember { mutableLongStateOf(0L) }
                        var duration by remember { mutableLongStateOf(1L) }
                        var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }

                        component.videoPlayer()(
                            modifier = Modifier.fillMaxSize(),
                            spec = VideoPlayer.Spec(
                                url = post.media,
                                ratio = post.aspectRatio,
                                resolution = post.resolution,
                                enabled = enabled,
                                onProgress = { p, d ->
                                    position = p
                                    duration = d.coerceAtLeast(1L)
                                },
                                onSeek = { newPos -> position = newPos },
                                onPlayerReady = { exoPlayer = it }
                            )
                        )

                        val uiContent = post.mapToContent()

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            AuthorView(
                                author = post.author,
                                description = post.time,
                                onClick = { onAuthorClick(post.author.id) },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(8.dp))

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

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 12.dp)
                                .width(56.dp)
                        ) {

                            EngagementScreen(
                                model = uiContent,
                                event = engagementEvent,
                                vertical = true
                            )

                            ModerationScreen(
                                uiContent,
                                moderationEvent
                            )
                        }

                        if (exoPlayer != null) {
                            VideoProgress(
                                player      = exoPlayer!!,
                                durationMs  = duration,
                                onSeek     = { pos -> exoPlayer!!.seekTo(pos) },
                                modifier    = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 0.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun UiEngagement.patchWith(r: UiReaction?) = copy(
    likes = likes + ((r?.isLiked == true && !isLiked).toInt()),
    isLiked = r?.isLiked ?: isLiked,
    dislikes = dislikes + ((r?.isDisliked == true && !isDisliked).toInt()),
    isDisliked = r?.isDisliked ?: isDisliked,
    comment = comment + (r?.commented ?: 0)
)







