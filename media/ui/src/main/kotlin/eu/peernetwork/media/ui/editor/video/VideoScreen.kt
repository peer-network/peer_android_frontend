package eu.peernetwork.media.ui.editor.video

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.ui.core.MediaPlayer
import java.io.File
import eu.peernetwork.media.ui.editor.video.Video as EditorVideo

@Composable
fun VideoScreen(
    url: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onVideoTrimmed: (File) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    val component = remember {
        provider.builder(EditorVideo.Builder::class.java).build(context)
    }

    val viewModel: VideoViewModel = viewModel(
        modelClass = VideoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )

    val videoPlayer = remember { component.videoPlayer() }
    val player = remember { (component.videoInteractor() as MediaPlayer).player() }

    LaunchedEffect(url) {
        viewModel.loadThumbs(url)
    }

    val currentState by viewModel.mutableState.collectAsStateWithLifecycle()

    val derivedState: State<DesignStatefulScaffoldState> = remember(currentState) {
        derivedStateOf {
            when (currentState) {
                is VideoViewModel.State.Ready ->
                    DesignStatefulScaffoldState.Success(currentState)

                is VideoViewModel.State.Exporting ->
                    DesignStatefulScaffoldState.Success((currentState as VideoViewModel.State.Exporting).base)

                is VideoViewModel.State.Complete ->
                    DesignStatefulScaffoldState.Success((currentState as VideoViewModel.State.Complete).base)

                is VideoViewModel.State.Failure ->
                    DesignStatefulScaffoldState.Error((currentState as VideoViewModel.State.Failure).cause)

                else -> DesignStatefulScaffoldState.Loading
            }
        }
    }

    val rawPercentage = when (currentState) {
        is VideoViewModel.State.Exporting -> (currentState as VideoViewModel.State.Exporting).pct
        is VideoViewModel.State.Complete -> 100
        else -> -1
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (rawPercentage >= 0) rawPercentage / 100f else 0f,
        label = "exportProgress"
    )

    LaunchedEffect(currentState) {
        if (currentState is VideoViewModel.State.Complete) {
            withFrameNanos { }
            onVideoTrimmed((currentState as VideoViewModel.State.Complete).file)
            viewModel.clearExport()
        }
    }

    DesignStatefulScaffold<VideoViewModel.State.Ready>(
        state = derivedState,
        onRefresh = { viewModel.loadThumbs(url) }
    ) { ready ->
        VideoContent(
            url = url,
            videoPlayer = videoPlayer,
            player = player,
            durationMs = ready.durationMs,
            trimStart = ready.trimStart,
            trimEnd = ready.trimEnd,
            frames = ready.frames,
            progressPct = (currentState as? VideoViewModel.State.Exporting)
                ?.let { animatedProgress },
            onTrimChange = viewModel::setTrim,
            onConfirm = { viewModel.confirmTrim(url, context.cacheDir) },
            onCancel = onCancel
        )
    }
}
