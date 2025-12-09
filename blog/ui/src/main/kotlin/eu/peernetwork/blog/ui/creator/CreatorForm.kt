package eu.peernetwork.blog.ui.creator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.extension.value
import eu.peernetwork.core.ui.mapper.annotate
import eu.peernetwork.core.ui.theme.DesignTheme

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
                focusRequester = focus,
                contentPadding = PaddingValues(16.dp),
                hint = stringResource(R.string.post_title),
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = VisualTransformation {
                    TransformedText(
                        text = title.value.annotate(),
                        offsetMapping = OffsetMapping.Identity
                    )
                },
            )
        }
        DesignTextField(
            state = description,
            verticalAlignment = Alignment.Top,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 64.dp,
            ),
            shape = RoundedCornerShape(24.dp),
            enabled = !isLoading.value,
            hint = stringResource(R.string.post_description),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Unspecified
            ),
            maxLength = 500,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            visualTransformation = VisualTransformation {
                TransformedText(
                    text = description.value.annotate(),
                    offsetMapping = OffsetMapping.Identity
                )
            },
            leading = {
                Text(
                    text = stringResource(R.string.description_label),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        )
    }
}

@Preview
@Composable
fun PreviewCreatorForm() {
    DesignTheme(isDarkMode = true) {
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
