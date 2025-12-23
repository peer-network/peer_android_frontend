package eu.peernetwork.media.ui.compose

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.media.core.R

@Composable
fun AudioScaffold(
    isEnabled: State<Boolean>,
    isPlaying: State<Boolean>,
    isLoading: State<Boolean>,
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    onPlayPauseClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val handlePlayPauseClick by rememberUpdatedState(onPlayPauseClick)
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = {
            handlePlayPauseClick()
        }, modifier = Modifier.size(36.dp).graphicsLayer {
            this.alpha = if (isLoading.value) alpha else 1f }
        ) {
            Crossfade(if (isPlaying.value && isEnabled.value) {
                R.drawable.ic_pause
            } else {
                R.drawable.ic_play
            }) { icon ->
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.scrim,
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
    DesignTheme {
        val state = remember { mutableStateOf(false) }
        val isLoading = remember { mutableStateOf(true) }
        AudioScaffold(
            isEnabled = state,
            isPlaying = state,
            isLoading = isLoading,
            onPlayPauseClick = { }
        ) { Text("...") }
    }
}
