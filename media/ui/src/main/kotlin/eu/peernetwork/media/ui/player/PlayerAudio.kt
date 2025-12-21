package eu.peernetwork.media.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import eu.peernetwork.media.ui.compose.AudioPlayerThumbnail
import eu.peernetwork.media.ui.interactor.MediaInteractor

@Composable
fun PlayerAudio(
    path: String,
//    cover: String?,
//    ratio: Float,
    expanded: Boolean,
    enabled: State<Boolean>,
    isPlaying: State<Boolean>,
    length: MutableLongState,
    modifier: Modifier = Modifier,
    interactor: MediaInteractor,
    onToggle: (Boolean) -> Unit
) {
    PlayerProvider(
        path = path,
        state = isPlaying,
        enabled = enabled,
        interactor = interactor,
    ) { player, isLoading ->
        Box(modifier = modifier) {
            AudioPlayerThumbnail(
                hasControls = expanded,
                enabled = enabled,
                isPlaying = isPlaying,
                isLoading = isLoading,
                length = length,
                session = interactor,
                source = { player },
                onPlay = onToggle
            )
        }
    }
}
