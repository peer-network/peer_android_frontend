package eu.peernetwork.media.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.R

@Composable
fun VolumeControl(
    mute: State<Boolean>,
    modifier: Modifier = Modifier,
    onMute: (Boolean) -> Unit
) {
    val handleOnMute by rememberUpdatedState(onMute)
    Box(
        modifier = Modifier.then(modifier)
            .padding(2.dp)
            .size(18.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.onBackground)
            .clickable(role = Role.Button, enabled = true) {
                handleOnMute(!mute.value)
            }.padding(4.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_mute),
            contentDescription = stringResource(eu.peernetwork.media.ui.R.string.un_mute_label),
            tint = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
                .graphicsLayer {
                    this.alpha = if (mute.value) 0f else 1f
                }
        )
        Icon(
            painter = painterResource(R.drawable.ic_unmute),
            contentDescription = stringResource(eu.peernetwork.media.ui.R.string.mute_label),
            tint = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
                .graphicsLayer {
                    this.alpha = if (mute.value) 1f else 0f
                }
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewVolumeControl() {
    PeerTheme {
        VolumeControl(
            remember { mutableStateOf(false) },
            modifier = Modifier.padding(16.dp)
        ) {}
    }
}
