package eu.peernetwork.media.ui.compose

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.R

@Composable
fun VideoControl(
    isLoading: State<Boolean>,
    isPlaying: State<Boolean>,
    error: State<Throwable?>,
    modifier: Modifier = Modifier,
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    onPlay: () -> Unit
) {
    val handleOnPlay by rememberUpdatedState(onPlay)
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.wrapContentSize()
            .clickable(enabled = true, role = Role.Button) {
                if (!isLoading.value) {
                    handleOnPlay()
                }
            }.then(modifier)
    ) {
        IconButton(
            { handleOnPlay() },
            modifier = Modifier.size(48.dp)
                .graphicsLayer {
                    this.alpha = if (!isPlaying.value && !isLoading.value && error.value == null) {
                        1f
                    } else {
                        0f
                    }
                }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_play),
                contentDescription = stringResource(eu.peernetwork.media.ui.R.string.video_label),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(
            { handleOnPlay() },
            modifier = Modifier.size(48.dp)
                .graphicsLayer {
                    this.alpha = if (error.value == null) {
                        0f
                    } else {
                        1f
                    }
                }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_replay),
                contentDescription = stringResource(eu.peernetwork.media.ui.R.string.video_label),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            stringResource(eu.peernetwork.core.ui.R.string.loading_text),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.graphicsLayer {
                this.alpha = if (isLoading.value && error.value == null) alpha else 0f
            }
        )
    }
}

@Composable
@Preview
fun PreviewVideoControl() {
    PeerTheme {
        VideoControl(
            remember { mutableStateOf(false) },
            remember { mutableStateOf(false) },
            remember { mutableStateOf(RuntimeException("Hello, world!")) },
        ) {}
    }
}
