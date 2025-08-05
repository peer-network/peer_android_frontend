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
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignThumbnail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.renderer.VideoPlayer
import eu.peernetwork.media.ui.compose.VolumeControl
import eu.peernetwork.media.ui.core.MediaPlayer
import eu.peernetwork.media.ui.extension.format
import eu.peernetwork.media.ui.extension.offset
import eu.peernetwork.media.ui.model.UiMetadata
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@Composable
@OptIn(FlowPreview::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
fun VideoScreen(
    path: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
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
    val canLoad = remember { derivedStateOf { !scrollState.isScrollInProgress } }
    val handleProceed by rememberUpdatedState(onProceed)
    DesignStatefulScaffold<UiMetadata>(
        state = derivedState,
        onRefresh = { viewModel.get(path) },
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize(),
        placeholder = { VideoScaffold() }
    ) {
        val duration = it.duration.format()
        val start = remember { mutableLongStateOf(0) }
        val stop = remember { mutableLongStateOf(duration) }
        val progress = remember { mutableFloatStateOf(0f) }
        val length = remember { mutableLongStateOf(duration) }
        val player = remember { (component.videoInteractor() as MediaPlayer).player() }
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
            onProceed = {
                handleProceed(
                    it.duration.offset(start.longValue),
                    it.duration.offset(stop.longValue),
                    it.duration
                )
            },
            thumbnail = {
                val key = "$path?time=$it"
                DesignThumbnail(
                    thumbnail = path,
                    visible = canLoad,
                    bitmap = thumbnail.value[key],
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                ) { path ->
                    viewModel.sync(
                        path,
                        key,
                        it * 1_000_000L
                    )
                }
            }
        ) {
            BoxWithConstraints(contentAlignment = Alignment.Center) {
                val key = "$path?blur=true"
                val width = with(density) { maxWidth.toPx().toInt() }
                val height = with(density) { maxHeight.toPx().toInt() }
                DesignThumbnail(
                    thumbnail = path,
                    visible = canLoad,
                    bitmap = thumbnail.value[key],
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                ) { path -> viewModel.background(path, width, height) }
                component.videoPlayer()(
                    Modifier.fillMaxHeight(),
                    VideoPlayer.Spec(
                        url = path,
                        ratio = it.height / it.width.toFloat(),
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
            LaunchedEffect(preview.value) {
                if (preview.value) {
                    player.play()
                } else {
                    player.pause()
                }
            }
            LaunchedEffect(progress.floatValue) {
                val startOffset = it.duration.offset(start.longValue)
                val endOffset = it.duration.offset(stop.longValue)
                if (player.currentPosition >= endOffset) {
                    player.seekTo(startOffset)
                } else if (player.currentPosition < startOffset) {
                    player.seekTo(startOffset)
                    player.play()
                }
            }
            LaunchedEffect(start.longValue, stop.longValue) {
                preview.value = false
                player.seekTo(it.duration.offset(start.longValue))
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.get(path)
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
    scrollState: LazyListState,
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
                scrollState = scrollState,
                onProceed = onProceed,
                thumbnail = thumbnail
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
            scrollState = scrollState,
            onProceed = { },
            thumbnail = {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer))
            }
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background))
        }
    }
}
