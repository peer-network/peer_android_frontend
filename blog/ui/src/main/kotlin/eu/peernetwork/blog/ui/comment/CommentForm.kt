package eu.peernetwork.blog.ui.comment

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.mapper.annotate
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun CommentForm(
    username: String,
    imageUrl: String,
    comment: TextFieldState,
    isLoading: State<Boolean>,
    isSuccess: State<Boolean>,
    onSubmit: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.height(48.dp)
        ) {
            DesignAvatar {
                DesignImage(
                    label = username,
                    imageUrl = imageUrl,
                    size = 42.dp,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.weight(1f)
                .padding(start = 10.dp)
        ) {
            DesignTextField(
                state = comment,
                shape = RoundedCornerShape(24.dp),
                hint = stringResource(R.string.post_reply),
                enabled = !isLoading.value,
                maxLength = 500,
                contentPadding = PaddingValues(
                    top = 16.dp,
                    start = 16.dp,
                    end = 56.dp,
                    bottom = 16.dp,
                ),
                visualTransformation = VisualTransformation {
                    TransformedText(
                        text = comment.text.toString().annotate(),
                        offsetMapping = OffsetMapping.Identity
                    )
                },
            )
            CommentButton(
                isLoading = isLoading,
                modifier = Modifier.padding(4.dp)
                    .align(Alignment.BottomEnd),
                onSubmit = onSubmit
            )
        }
        LaunchedEffect(isLoading.value) {
            if (isSuccess.value) {
                comment.clearText()
            }
        }
        DisposableEffect(Unit) {
            onDispose { comment.clearText() }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewCommentForm() {
    DesignTheme {
        val comment = remember { TextFieldState() }
        val isLoading = remember { mutableStateOf(false) }
        val isSuccess = remember { mutableStateOf(true) }
        CommentForm(
            username = "John Doe",
            imageUrl = "http://localhost",
            comment = comment,
            isLoading = isLoading,
            isSuccess = isSuccess
        ) {}
    }
}
