package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.ui.design.material.DesignBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScaffold(
    state: MutableState<UiContent?>,
    canDismiss: () -> Boolean,
    onDismiss: () -> Unit,
    sheet: @Composable () -> Unit,
    content: @Composable (State<Size>) -> Unit
) {
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    val updatedSheet by rememberUpdatedState(sheet)
    val updatedContent by rememberUpdatedState(content)
    val handleDismiss by rememberUpdatedState(onDismiss)
    DesignBottomSheet(
        state = showSheet,
        canDismiss = canDismiss,
        onDismiss = {
            state.value = null
            handleDismiss()
        },
        color = MaterialTheme.colorScheme.tertiaryContainer,
        footer = {
            Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .navigationBarsPadding()
                .fillMaxWidth()) {
                updatedSheet()
            }
        },
        content = {
            Box(modifier = Modifier.statusBarsPadding()) {
                updatedContent(it)
            }
        }
    )
}
