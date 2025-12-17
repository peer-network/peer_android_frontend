package eu.peernetwork.app.ui.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.app.mapper.label
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun SearchToolbar(
    state: TextFieldState,
    mode: MutableState<SearchMode>,
    modifier: Modifier = Modifier
) {
    val focus = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var lastMode by remember { mutableStateOf<SearchMode>(SearchMode.Username) }
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Crossfade(
            targetState = mode.value,
            modifier = Modifier.fillMaxWidth()
        ) { target ->
            if (target == SearchMode.Default) {
                SearchLabel(Modifier.padding(horizontal = 8.dp)) {
                    mode.value = it
                    lastMode = it
                }
            } else {
                SearchField(
                    state = state,
                    mode = target,
                    focus = focus
                )
                LaunchedEffect(mode.value) {
                    if (mode.value != SearchMode.Default) {
                        focus.requestFocus()
                        keyboardController?.show()
                    }
                }
            }
        }
        IconButton(
            onClick = {
                state.clearText()
                mode.value = if (mode.value !is SearchMode.Default) {
                    SearchMode.Default
                } else {
                    lastMode
                }
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = if (mode.value is SearchMode.Default) {
                    painterResource(R.drawable.ic_search_outline)
                } else {
                    painterResource(R.drawable.ic_return)
                },
                contentDescription = mode.value.label(),
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchToolbar() {
    DesignTheme {
        val state = remember { TextFieldState() }
        val mode = remember { mutableStateOf<SearchMode>(SearchMode.Username) }
        SearchToolbar(
            state,
            mode
        )
    }
}
