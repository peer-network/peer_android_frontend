package eu.peernetwork.app.ui.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignCard
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.delay

enum class SearchMode(
    val value: Int,
    val symbol: String
) {
    USERNAME(eu.peernetwork.app.R.string.mention_label, "@"),
    TAG(eu.peernetwork.app.R.string.tag_label, "#"),
    TITLE(eu.peernetwork.app.R.string.title_label, "~")
}

@Composable
fun SearchHeader(
    state: TextFieldState,
    mode: MutableState<SearchMode?>,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focus = remember { FocusRequester() }
    var lastMode by remember { mutableStateOf<SearchMode>(mode.value ?: SearchMode.USERNAME) }
    DesignCard(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentPadding = PaddingValues(vertical = 0.dp),
        trailing = {
            IconButton(onClick = {
                state.clearText()
                mode.value = if (mode.value != null) {
                    null
                } else {
                    lastMode
                }
            }) {
                Icon(
                    painter = mode.value?.let { painterResource(R.drawable.ic_return) }
                        ?: painterResource(R.drawable.ic_search_outline),
                    contentDescription = mode.value?.let { stringResource(it.value) },
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        },
        modifier = modifier
    ) {
        Crossfade(targetState = mode.value) { target ->
            if (target == null) {
                SearchBar(Modifier.padding(horizontal = 8.dp)) {
                    mode.value = it
                    lastMode = it
                }
            } else {
                DesignTextField(
                    state,
                    enabled = mode.value != null,
                    focusRequester = focus,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceTint,
                    ),
                    leading = {
                        Text(
                            lastMode.symbol,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.tertiary
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    },
                ) { Text(stringResource(R.string.search_label)) }
            }
        }
    }
    LaunchedEffect(mode.value) {
        if (mode.value != null) {
            delay(200)
            focus.requestFocus()
            keyboardController?.show()
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onClick: (SearchMode) -> Unit,
) {
    Row(modifier = modifier) {
        SearchBarItem(SearchMode.USERNAME, onClick = onClick)
        Spacer(modifier = Modifier.width(8.dp))
        SearchBarItem(SearchMode.TAG, onClick = onClick)
        Spacer(modifier = Modifier.width(8.dp))
        SearchBarItem(SearchMode.TITLE, onClick = onClick)
    }
}

@Composable
fun SearchBarItem(mode: SearchMode, onClick: (SearchMode) -> Unit) { Text(
        "${mode.symbol}${stringResource(mode.value)}",
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.tertiary
        ),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .clickable(role = Role.Button, onClick = { onClick(mode) })
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            )
    )
}

@Preview
@Composable
fun PreviewSearchHeader() {
    PeerTheme {
        val state = remember { TextFieldState() }
        val mode = remember { mutableStateOf<SearchMode?>(null) }
        SearchHeader(
            state,
            mode
        )
    }
}
