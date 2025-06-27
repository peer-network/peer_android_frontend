package eu.peernetwork.blog.ui.timeline.video

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Color.TRANSPARENT
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
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
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.moderation.Moderations
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.renderer.VideoPlayer
import kotlinx.coroutines.flow.Flow

@Composable
fun VideoOverlay(
    id: String,
    limit: Int,
    position: Int,
    enabled: Boolean,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAuthorClick : (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    connection     : @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit,
    engagementEvent: Engagements,
    moderationEvent: Moderations,
    onLoadBitmap: (String) -> Bitmap?,
    onLoad       : (String, Float) -> Unit         = { _, _ -> },
) {

    val ctx = LocalContext.current
    DisposableEffect(Unit) {
        val activity = ctx as? Activity
        val oldOrientation = activity?.requestedOrientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val w = (ctx as? Activity)?.window
        val oldStatus = w?.statusBarColor
        val oldNav    = w?.navigationBarColor
        w?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
            it.statusBarColor     = TRANSPARENT
            it.navigationBarColor = TRANSPARENT
        }
        onDispose {
            w?.let {
                it.statusBarColor     = oldStatus ?: TRANSPARENT
                it.navigationBarColor = oldNav    ?: TRANSPARENT
                WindowCompat.setDecorFitsSystemWindows(it, true)
            }
        }
    }

    val component = remember { provider.builder(Video.Builder::class.java).build(ctx) }
    val viewModel = viewModel(
        modelClass          = VideoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory             = component.viewModelFactory()
    )
    val vmState by viewModel.state.collectAsStateWithLifecycle()

    val scaffoldState: State<DesignStatefulScaffoldState> = remember(vmState) {
        derivedStateOf {
            when (val s = vmState) {
                VideoViewModel.State.Empty      -> DesignStatefulScaffoldState.Empty
                VideoViewModel.State.Loading    -> DesignStatefulScaffoldState.Loading
                is VideoViewModel.State.Success -> DesignStatefulScaffoldState.Success(s.data)
                is VideoViewModel.State.Error   -> DesignStatefulScaffoldState.Error(s.error)
            }
        }
    }

    val pull = rememberPullRefreshState(
        refreshing = false,
        onRefresh  = { viewModel.load(Pageable(0, limit)) }
    )

    DragRefreshLayout(state = pull) {
        DesignStatefulScaffold<Flow<PagingData<UiVideo>>>(
            state     = scaffoldState,
            onRefresh = { viewModel.load(Pageable(0, limit)) }
        ) { flow ->

            val items = flow.collectAsLazyPagingItems()
            if (items.loadState.refresh is LoadState.Loading) {
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                return@DesignStatefulScaffold
            }

            val pagerState = rememberPagerState(
                pageCount   = { maxOf(items.itemCount, 1) },
                initialPage = position
            )

            VerticalPager(state = pagerState) { page ->
                val post = items[page] ?: run {
                    Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                    return@VerticalPager
                }
                key(post.media) {
                    Box(Modifier.fillMaxSize()) {

                        var curPos by remember { mutableLongStateOf(0L) }
                        var durMs  by remember { mutableLongStateOf(1L) }
                        var exo    by remember { mutableStateOf<ExoPlayer?>(null) }
                        var videoRatio by remember { mutableFloatStateOf(post.aspectRatio) }
                        var frameRendered by remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .blur(24.dp)
                                .graphicsLayer { alpha = 0.65f }
                        ) {
                            DesignThumbnail(
                                thumbnail    = post.media,
                                bitmap       = onLoadBitmap(post.media),
                                contentScale = ContentScale.Crop,
                                onRefresh    = { onLoad(post.media, post.aspectRatio) }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .let { mod ->
                                    if (frameRendered && !videoRatio.isNaN())
                                        mod.aspectRatio(videoRatio).fillMaxHeight()
                                    else
                                        mod.fillMaxSize()
                                }
                        ) {
                            fun updateRatio(vs: VideoSize) {
                                if (vs.width == 0 || vs.height == 0) return

                                val rawW = vs.width.toFloat()
                                val rawH = vs.height.toFloat()

                                val rotated =
                                    if (vs.unappliedRotationDegrees == 90 || vs.unappliedRotationDegrees == 270)
                                        rawH / rawW
                                    else
                                        rawW / rawH

                                videoRatio = rotated.coerceAtLeast(0.01f)
                            }
                            component.videoPlayer()(
                                modifier = Modifier.matchParentSize(),
                                spec = VideoPlayer.Spec(
                                    url           = post.media,
                                    ratio         = post.aspectRatio,
                                    resolution    = post.resolution,
                                    enabled       = enabled,
                                    onProgress    = { p, d -> curPos = p; durMs = d.coerceAtLeast(1L) },
                                    onSeek = {
                                        curPos = it
                                        exo?.seekTo(it)
                                    },
                                    onPlayerReady = { player ->
                                        exo = player
                                        frameRendered = false
                                        updateRatio(player.videoSize)

                                        player.addListener(object : Player.Listener {
                                            override fun onVideoSizeChanged(newVideoSize: VideoSize) {
                                                updateRatio(newVideoSize)
                                            }

                                            override fun onRenderedFirstFrame() {
                                                frameRendered = true
                                            }
                                        })
                                    }
                                )
                            )
                        }

                        exo?.let { player ->
                            VideoProgress(
                                player     = player,
                                durationMs = durMs,
                                onSeek     = { player.seekTo(it) },
                                modifier   = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(
                                        bottom = WindowInsets.navigationBars
                                            .asPaddingValues()
                                            .calculateBottomPadding()
                                    )
                                    .fillMaxWidth()
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(
                                    bottom = WindowInsets.navigationBars
                                        .asPaddingValues()
                                        .calculateBottomPadding() + 48.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                        ) {
                                AuthorView(
                                    author      = post.author,
                                    description = post.time,
                                    onClick     = { onAuthorClick(post.author.id) },
                                    modifier    = Modifier.weight(1f)
                                )

                            if (id != post.author.id) {
                                connection(
                                    Triple(
                                        post.author.id,
                                        post.author.isfollowing,
                                        post.author.isfollowed
                                    )
                                )
                            }
                        }

                        val uiContent = post.mapToContent()
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 12.dp)
                                .width(56.dp)
                        ) {
                            EngagementScreen(uiContent, engagementEvent, vertical = true)
                            ModerationScreen (uiContent, moderationEvent)
                        }
                    }
                    }
                }
            }
        }
    }
