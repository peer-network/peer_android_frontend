package eu.peernetwork.ads.ui.boost

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import eu.peernetwork.ads.ui.model.UiBoost
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignDialog

@Composable
fun BoostModal(
    state: MutableState<UiBoost?>,
    onConfirm: (String) -> Unit,
) {
    val handleConfirm by rememberUpdatedState(onConfirm)
    val streamState = remember { derivedStateOf {
        state.value?.let {
            DesignStreamState.Success(it)
        } ?: DesignStreamState.Default
    } }
    val showDialog = remember { derivedStateOf { streamState.value is DesignStreamState.Success } }
    DesignDialog(
        state = showDialog,
        dim = true,
        onDismiss = { state.value = null }
    ) { controller, progress, dialogState ->
        DesignStream(streamState) { target ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
                    .graphicsLayer {
                        alpha = progress.value
                    }
            ) {
                if (target.value.isReported) {
                    BoostReportedContentLauncher(onCancel = { state.value = null }) {
                        handleConfirm(target.value.id)
                        state.value = null
                    }
                } else if (!target.value.isAccessible) {
                    BoostHiddenContentLauncher(onCancel = { state.value = null }) {
                        handleConfirm(target.value.id)
                        state.value = null
                    }
                } else {
                    BoostLauncher(onCancel = { state.value = null }) {
                        handleConfirm(target.value.id)
                        state.value = null
                    }
                }
            }
        }
    }
}
