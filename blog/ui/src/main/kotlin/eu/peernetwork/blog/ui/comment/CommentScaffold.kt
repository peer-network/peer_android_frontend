package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignDialogSheet
import eu.peernetwork.core.ui.design.compose.DesignOverlayBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScaffold(
    tag: String,
    state: MutableState<UiContent?>,
    modifier: Modifier = Modifier,
    sheet: @Composable (State<Boolean>) -> Unit,
    content: @Composable (State<Boolean>) -> Unit
) {
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    DesignBottomSheet(
        showSheet = showSheet,
        tag = "commentBottomSheet#${tag}",
        modifier = modifier,
        onDismissRequest = { state.value = null },
        color = MaterialTheme.colorScheme.tertiaryContainer,
        background = {
            DesignOverlayBackground(
                state = it,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = .6f))
            )
        },
        content = { content(it) }
    )
    DesignDialogSheet(
        "commentDesignBottomSheet#${tag}",
        background = {},
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
        visible = showSheet.value
    ) { overlayState ->
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .navigationBarsPadding()
            .fillMaxWidth()) {
            sheet(overlayState)
        }
    }
}
