package eu.peernetwork.blog.ui.comment

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.compose.PostSummary
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.design.compose.DesignRichTextField
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.theme.PeerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentForm(
    model: UiContent,
    comment: TextFieldState,
    isLoading: State<Boolean>,
    replyTo: MutableState<String?>,
    modifier: Modifier = Modifier,
    onSubmit: (String, String) -> Unit = { id, comment -> },
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
) {
    val focus = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val derivedState = remember { derivedStateOf { comment.isValidInput() } }
    Column {
        Box(modifier = Modifier.height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceDim.copy(alpha = .1f)))
        Column(modifier = modifier) {
            PostSummary(
                model = model,
                modifier = Modifier.padding(top = 16.dp, end = 16.dp),
                onMentionClick = onMentionClick,
                onHashtagClick = onHashtagClick
            ) {}
            DesignRichTextField(
                state = comment,
                enabled = !isLoading.value,
                modifier = Modifier.padding(start = 36.dp),
                focusRequester = focus,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Unspecified
                ),
                maxLength = 500,
                maxLines = 6,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceTint,
                ),
                trailing = {
                    DesignOutlinedButton(
                        onClick = { onSubmit(model.id, comment.text.toString()) },
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(28),
                        ),
                        enabled = derivedState.value,
                        isLoading = isLoading.value && derivedState.value,
                        shape = RoundedCornerShape(28),
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = MaterialTheme.colorScheme.surfaceVariant
                                .copy(alpha = .3f),
                        ),
                        minHeight = 32.dp,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp)
                    ) { Text(stringResource(R.string.comment_label)) }
                },
            ) { Text(stringResource(R.string.post_reply)) }
            Spacer(modifier = Modifier.height(24.dp))
            LaunchedEffect(replyTo.value) {
                if (replyTo.value != null) {
                    comment.setTextAndPlaceCursorAtEnd("@${replyTo.value} ")
                    focus.requestFocus()
                    keyboardController?.show()
                    replyTo.value = null
                }
            }
            DisposableEffect(Unit) {
                onDispose {
                    if (replyTo.value == null) {
                        comment.clearText()
                    }
                }
            }
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
            replyTo = remember { mutableStateOf(null) },
            model = UiContent(
                id = "abc123",
                title = buildAnnotatedString { append("John Doe") },
                author = UiAuthor(
                    id = "",
                    slug = 12034,
                    username = "JohnDoe",
                    imageUrl = "http://localhost",
                    isfollowing = false,
                    isfollowed = false
                ),
                createdAt = System.currentTimeMillis(),
                description = buildAnnotatedString {
                    append("This is a mock description for a content post. It's purely for testing.")
                },
                likes = 25,
                isLiked = true,
                isDisliked = false,
                dislikes = 3,
                views = 2,
                comment = 5
            )
        )
    }
}
