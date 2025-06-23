package eu.peernetwork.media.ui.compose

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.ui.R

@Composable
fun VideoControl(
    isLoading: State<Boolean>,
    isPlaying: State<Boolean>,
    isProcessing: State<Boolean>,
    mute: State<Boolean>,
    progress: State<Float>,
    onUpdate: (Float) -> Unit,
    onMute: (Boolean) -> Unit,
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    onPlay: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )
    var isSeeking by remember { mutableStateOf(false) }
    val handleOnPlay by rememberUpdatedState(onPlay)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            {
                if (!isLoading.value) {
                    handleOnPlay()
                }
            },
            modifier = Modifier.size(48.dp)
                .graphicsLayer {
                    this.alpha = if (isProcessing.value && !isPlaying.value && !isLoading.value) {
                        alpha
                    } else if (!isPlaying.value && !isLoading.value) {
                        1f
                    } else {
                        0f
                    }
                }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_play),
                contentDescription = stringResource(R.string.video_label),
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
                this.alpha = if (isLoading.value) alpha else 0f
            }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(24.dp)
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .height(3.dp)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { isSeeking = true },
                            onDragEnd = { isSeeking = false },
                            onHorizontalDrag = { change, _ ->
                                onUpdate((change.position.x / size.width).coerceIn(0f, 1f))
                            }
                        )
                    }.clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.value)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.tertiary)
                )
            }
            VolumeControl(mute, onMute)
        }
    }
}

@Composable
@Preview
fun PreviewVideoControl() {
    PeerTheme {
        VideoControl(
            remember { mutableStateOf(false) },
            remember { mutableStateOf(false) },
            remember { mutableStateOf(false) },
            remember { mutableStateOf(false) },
            remember { mutableFloatStateOf(0.5f) },
            {},
            {}
        ) {}
    }
}
