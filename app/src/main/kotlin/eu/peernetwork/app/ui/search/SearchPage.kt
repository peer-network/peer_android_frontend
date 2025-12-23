package eu.peernetwork.app.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun SearchPage(
    state: TextFieldState,
    mode: MutableState<SearchMode>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignScaffold {
        Box(modifier = modifier.fillMaxSize()) {
            updatedContent()
            SearchToolbar(
                state = state,
                mode = mode,
                modifier = Modifier.padding(horizontal = 12.dp)
                    .padding(top = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchPage() {
    DesignTheme {
        val state = remember { TextFieldState() }
        val mode = remember { mutableStateOf<SearchMode>(SearchMode.Default) }
        SearchPage(
            state,
            mode
        ) {}
    }
}
