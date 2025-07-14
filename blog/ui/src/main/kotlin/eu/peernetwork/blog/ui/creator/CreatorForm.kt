package eu.peernetwork.blog.ui.creator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.core.ui.design.compose.DesignRichTextField
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun CreatorForm(
    title: TextFieldState,
    focus: FocusRequester,
    description: TextFieldState,
    isLoading: State<Boolean>,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DesignTextField(
                state = title,
                enabled = !isLoading.value,
                modifier = Modifier.weight(1f),
                focusRequester = focus,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            ) { Text(stringResource(R.string.post_title)) }
        }
        DesignRichTextField(
            description,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 36.dp,
            ),
            enabled = !isLoading.value,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            verticalAlignment = Alignment.Top,
            maxLines = 3,
            maxLength = 500,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            leading = {
                Text(
                    text = stringResource(R.string.description_label),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .padding(bottom = 48.dp)
                )
            }
        ) { Text(text = stringResource(R.string.post_description)) }
    }
}

@Preview
@Composable
fun PreviewCreatorForm() {
    PeerTheme(isDarkMode = true) {
        val title = remember { TextFieldState() }
        val description = remember { TextFieldState() }
        val focus = remember { FocusRequester() }
        CreatorForm(
            title = title,
            focus = focus,
            description = description,
            isLoading = remember { mutableStateOf(false) }
        )
    }
}
