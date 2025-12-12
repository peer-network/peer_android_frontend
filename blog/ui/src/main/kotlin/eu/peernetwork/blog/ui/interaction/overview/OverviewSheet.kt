package eu.peernetwork.blog.ui.interaction.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignCollapsibleBottomSheet

@Composable
fun OverviewSheet(
    state: MutableState<UiEngagement?>,
    content: @Composable (UiEngagement) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val sheetState = remember { derivedStateOf {
        if (state.value == null) {
            DesignStreamState.Default
        } else {
            DesignStreamState.Success(state.value!!)
        }
    } }
    val showSheet = remember { derivedStateOf { state.value != null } }
    DesignStream(sheetState) { id ->
        DesignCollapsibleBottomSheet(
            state = showSheet,
            peekHeight = 400.dp,
            onDismiss = { state.value = null }
        ) { updatedContent(id.value) }
    }
}
