package eu.peernetwork.media.ui.renderer

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.media.core.renderer.MediaController
import eu.peernetwork.media.ui.R
import eu.peernetwork.media.ui.interactor.MediaInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaControllerDelegate @Inject constructor(
    private val session: MediaInteractor
) : MediaController {
    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: MediaController.Spec
    ) {}

    @Composable
    override fun Volume() {
        val scope = rememberCoroutineScope()
        val mute = session.volume().collectAsStateWithLifecycle(
            initialValue = session.exoPlayer().isDeviceMuted
        )
        IconButton(
            onClick = {
                scope.launch { session.unmute(!mute.value) }
            },
            modifier = Modifier.size(28.dp),
        ) {
            Icon(
                painter = painterResource(if (mute.value) {
                    R.drawable.ic_unmuted
                } else {
                    R.drawable.ic_muted
                }),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
