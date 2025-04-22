package eu.peernetwork.blog.ui.comment

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.compose.ContentBadge
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.ui.design.compose.DesignTextButton
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.theme.PeerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentForm(
    model: UiContent,
    comment: TextFieldState,
    isLoading: State<Boolean>,
    modifier: Modifier = Modifier,
    onSubmit: (String, String) -> Unit = { id, comment -> }
) {
    Column {
        Box(modifier = Modifier.height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceDim.copy(alpha = .1f)))
        Column(modifier = modifier) {
            ContentBadge(
                model = model,
                modifier = Modifier.padding(top = 16.dp)
            ) {}
            DesignTextField(
                state = comment,
                enabled = !isLoading.value,
                modifier = Modifier.padding(start = 36.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                onKeyboardAction = KeyboardActions {
                    if (comment.isValidInput())
                    onSubmit(model.id, comment.text.toString()) },
                maxLines = 3,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceTint,
                ),
                trailing = {
                    DesignTextButton(
                        onClick = { onSubmit(model.id, comment.text.toString()) },
                        enabled = comment.isValidInput(),
                        isLoading = isLoading.value
                    ) {
                        Text(
                            stringResource(R.string.send_label),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            ) { Text(stringResource(R.string.post_reply)) }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewCommentForm() {
    PeerTheme {
        val comment = remember { TextFieldState() }
        val isLoading = remember { mutableStateOf(false) }
        CommentForm(
            comment = comment,
            modifier = Modifier.padding(horizontal = 24.dp),
            isLoading = isLoading,
            model = UiContent(
                id = "abc123",
                title = "John Doe",
                author = UiAuthor(
                    id = "",
                    slug = 12034,
                    username = "JohnDoe",
                    imageUrl = "http://localhost"
                ),
                createdAt = System.currentTimeMillis(),
                description = "This is a mock description for a content post. It's purely for testing.",
                likes = 25,
                isLiked = true,
                isDisliked = false,
                dislikes = 3,
                comment = 5
            )
        )
    }
}
