package eu.peernetwork.blog.ui.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import eu.peernetwork.core.ui.design.compose.DesignOverlayBackground
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.media.core.model.MimeType

@Composable
fun CreatorScaffold(
    isLoading: State<Boolean>,
    type: State<MimeType?>,
    modifier: Modifier = Modifier,
    footer: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val overlayState = remember { derivedStateOf { isLoading.value || type.value == null } }
    DesignScaffold(
        modifier = modifier.fillMaxSize(),
        alwaysReturn = true,
        header = { content() },
        overlay = {}
    ) { state ->
        Box {
            footer()
            DesignOverlayBackground(
                state = overlayState,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = .8f))
            )
        }
    }
    LaunchedEffect(overlayState.value) {
        if (!overlayState.value) {
            focusManager.clearFocus()
        }
    }
}
