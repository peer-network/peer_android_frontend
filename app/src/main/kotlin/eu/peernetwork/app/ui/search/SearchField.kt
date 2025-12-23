package eu.peernetwork.app.ui.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.mapper.symbol
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun SearchField(
    state: TextFieldState,
    mode: SearchMode,
    focus: FocusRequester,
) {
    DesignTextField(
        state = state,
        enabled = mode != SearchMode.Default,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        shape = RectangleShape,
        hint = stringResource(R.string.search_label),
        contentPadding = PaddingValues(vertical = 16.dp),
        minLines = 1,
        leading = {
            Text(
                text = mode.symbol,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    start = 14.dp,
                    end = 8.dp
                )
            )
        },
        modifier = Modifier.focusRequester(focus)
    )
}

@Preview
@Composable
fun PreviewSearchField() {
    DesignTheme(isDarkMode = true) {
        val focus = remember { FocusRequester() }
        val state = remember { TextFieldState() }
        SearchField(
            state,
            SearchMode.Username,
            focus
        )
    }
}
