package eu.peernetwork.blog.ui.creator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceTint,
                ),
            ) { Text(stringResource(R.string.post_title)) }
        }
        DesignRichTextField(
            description,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 48.dp,
            ),
            enabled = !isLoading.value,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            verticalAlignment = Alignment.Top,
            maxLines = 3,
            maxLength = 500,
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 12.dp),
            leading = { },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceTint,
            ),
        ) { Text(text = stringResource(R.string.post_description)) }
        Spacer(modifier = Modifier.height(4.dp))
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
