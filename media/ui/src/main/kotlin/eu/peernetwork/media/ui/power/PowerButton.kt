package eu.peernetwork.media.ui.power

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.media.ui.R

@Composable
fun PowerButton(
    translucent: Boolean = false,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(72.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        if (translucent) {
            Box(modifier = Modifier.fillMaxSize()
                .background(Color.Black.copy(alpha = .3f)))
        } else {
            Box(modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLow))
        }
        updatedContent()
    }
}

@Composable
fun PowerButton(
    state: State<Boolean>,
    translucent: Boolean = false,
    onClick: () -> Unit
) {
    PowerButton(
        translucent = translucent,
        onClick = onClick
    ) {
        Crossfade(state.value) { target ->
            if (target) {
                Icon(
                    painter = painterResource(R.drawable.ic_pause),
                    contentDescription = stringResource(R.string.play_pause),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_play),
                    contentDescription = stringResource(R.string.play_pause),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewPowerButton() {
    DesignTheme(isDarkMode = true) {
        val state = remember { mutableStateOf(false) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { PowerButton(state) {} }
    }
}
