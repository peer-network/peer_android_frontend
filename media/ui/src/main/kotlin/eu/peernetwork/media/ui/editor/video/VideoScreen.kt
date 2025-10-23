package eu.peernetwork.media.ui.editor.video

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.material.DesignThumbnail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.renderer.VideoPlayer
import eu.peernetwork.media.ui.compose.VolumeControl
import eu.peernetwork.media.ui.interactor.MediaInteractor
import eu.peernetwork.media.ui.extension.format
import eu.peernetwork.media.ui.extension.offset
import eu.peernetwork.media.core.model.UiMediaProperty
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce

@Composable
@OptIn(FlowPreview::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
fun VideoScreen(
    path: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onDiscard: () -> Unit,
    onProceed: (Long, Long, Long) -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val component = remember {
        provider.builder(Video.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = VideoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when (state) {
            VideoViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
            VideoViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
            is VideoViewModel.State.Success -> DesignStatefulScaffoldState.Success(
                (state as VideoViewModel.State.Success).data
            )
            is VideoViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                (state as VideoViewModel.State.Error).error
            )
        }
    } }
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    val scrollState = rememberLazyListState()
    val enable = remember { derivedStateOf { !scrollState.isScrollInProgress } }
    val handleProceed by rememberUpdatedState(onProceed)
    DesignStatefulScaffold<UiMediaProperty>(
        state = derivedState,
        onRefresh = { viewModel.get(path) },
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize(),
        placeholder = { VideoScaffold() }
    ) { metaData ->
        val duration = metaData.duration.format()
        val start = remember { mutableLongStateOf(0) }
        val stop = remember { mutableLongStateOf(duration) }
        val progress = remember { mutableFloatStateOf(0f) }
        val length = remember { mutableLongStateOf(duration) }
        val player = remember { (component.videoInteractor() as MediaInteractor).exoPlayer() }
        val isReady = remember { mutableStateOf(false) }
        val preview = remember { mutableStateOf(false) }
        val mute = remember { mutableStateOf(true) }
        VideoScreen(
            start = start,
            stop = stop,
            mute = mute,
            scrollState = scrollState,
            duration = duration,
            frameSize = duration.coerceAtMost(5).toInt(),
            minFrameSize = 2,
            timestamp = metaData.duration,
            onDiscard = onDiscard,
            onProceed = {
                handleProceed(
                    metaData.duration.offset(start.longValue),
                    metaData.duration.offset(stop.longValue),
                    metaData.duration
                )
            },
            thumbnail = { time ->
                val key = "$path?time=$time"
                val bitmap = remember { derivedStateOf { thumbnail.value[key] } }
                DesignThumbnail(
                    thumbnail = path,
                    enable = enable,
                    bitmap = bitmap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) { path ->
                    viewModel.videoThumbnail(
                        path,
                        key,
                        time * 1_000_000L
                    )
                }
            }
        ) {
            BoxWithConstraints(contentAlignment = Alignment.Center) {
                val key = "$path?blur=true"
                val width = with(density) { maxWidth.toPx().toInt() }
                val height = with(density) { maxHeight.toPx().toInt() }
                val bitmap = remember { derivedStateOf { thumbnail.value[key] } }
                DesignThumbnail(
                    thumbnail = path,
                    enable = enable,
                    bitmap = bitmap,
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) { path -> viewModel.cover(path, width, height) }
                component.videoPlayer()(
                    Modifier.fillMaxHeight(),
                    VideoPlayer.Spec(
                        url = path,
                        ratio = metaData.height / metaData.width.toFloat(),
                        progress = progress,
                        resolution = Pair(width, height),
                        length = length,
                        enabled =  isReady.value
                    )
                )
                LaunchedEffect(mute.value) {
                    player.volume = if (mute.value) {
                        1f
                    } else {
                        0f
                    }
                }
            }
            LaunchedEffect(Unit) {
                snapshotFlow { stop.longValue - start.longValue }
                    .debounce(300)
                    .collect {
                        if (isReady.value) {
                            preview.value = true
                        }
                        isReady.value = true
                    }
            }
            LaunchedEffect(Unit) {
                snapshotFlow { preview.value }
                    .collect { value ->
                        if (value) {
                            player.play()
                        } else {
                            player.pause()
                        }
                    }
            }
            LaunchedEffect(Unit) {
                snapshotFlow { progress.floatValue }
                    .collect { value ->
                        val startOffset = metaData.duration.offset(start.longValue)
                        val endOffset = metaData.duration.offset(stop.longValue)
                        if (player.currentPosition >= endOffset) {
                            player.seekTo(startOffset)
                        } else if (player.currentPosition < startOffset) {
                            player.seekTo(startOffset)
                            player.play()
                        }
                    }
            }
            LaunchedEffect(Unit) {
                combine(
                    snapshotFlow { start.longValue },
                    snapshotFlow { stop.longValue }
                ) { begin, end -> Pair(begin, end) }.collectLatest {
                    preview.value = false
                    player.seekTo(metaData.duration.offset(it.first))
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.get(path)
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun VideoScreen(
    start: MutableState<Long>,
    stop: MutableState<Long>,
    mute: MutableState<Boolean>,
    duration: Long,
    frameSize: Int,
    minFrameSize: Int,
    timestamp: Long,
    scrollState: LazyListState,
    onDiscard: () -> Unit,
    onProceed: () -> Unit,
    thumbnail: @Composable (Long) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    VideoScaffold(
        footer = {
            VideoFooter(
                start = start,
                stop = stop,
                duration = duration,
                frameSize = frameSize,
                minFrameSize = minFrameSize,
                timestamp = timestamp,
                scrollState = scrollState,
                onDiscard = onDiscard,
                onProceed = onProceed,
                thumbnail = thumbnail,
            )
        }
    ) {
        updatedContent()
        VolumeControl(
            mute,
            modifier = Modifier.padding(16.dp)
                .align(Alignment.BottomEnd)
        ) { mute.value = it }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewVideoScreen() {
    PeerTheme {
        val start = remember { mutableLongStateOf(2) }
        val stop = remember { mutableLongStateOf(4) }
        val mute = remember { mutableStateOf(false) }
        val scrollState = rememberLazyListState()
        VideoScreen(
            start = start,
            stop = stop,
            mute = mute,
            duration = 60,
            frameSize = 5,
            minFrameSize = 2,
            timestamp = 2,
            scrollState = scrollState,
            onDiscard = { },
            onProceed = { },
            thumbnail = {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant))
            }
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant))
        }
    }
}
