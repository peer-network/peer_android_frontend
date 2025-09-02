package eu.peernetwork.media.ui.compose

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.R

@Composable
fun AudioScaffold(
    isPlaying: State<Boolean>,
    onPlayPauseClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val handlePlayPauseClick by rememberUpdatedState(onPlayPauseClick)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {
            handlePlayPauseClick()
        }, modifier = Modifier.size(36.dp)) {
            Crossfade(if (isPlaying.value) {
                R.drawable.ic_pause
            } else {
                R.drawable.ic_play
            }) { icon ->
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        updatedContent()
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewAudioScaffold() {
    PeerTheme {
        val state = remember { mutableStateOf(false) }
        AudioScaffold(
            isPlaying = state,
            onPlayPauseClick = { }
        ) { Text("...") }
    }
}
