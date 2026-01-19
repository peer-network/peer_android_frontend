package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.core.ui.theme.DesignTheme

private const val tag = "CLICK_TO_SEE"

@Composable
fun CommentMask(
    status: UiStatus,
    isAccessible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val isVisible = remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        if (status == UiStatus.ILLEGAL) {
            CommentMask()
        } else if (!isAccessible) {
            if (isVisible.value) {
                updatedContent()
            } else {
                CommentMask { isVisible.value = true }
            }
        } else {
            updatedContent()
        }
    }
}

@Composable
fun CommentMask(onClick: () -> Unit) {
    var layoutResult: TextLayoutResult? = null
    val handleClick by rememberUpdatedState(onClick)
    val prompt = stringResource(R.string.hidden_content_prompt)
    val annotatedString = buildAnnotatedString {
        append(prompt)
        append(" ")
        pushStringAnnotation(tag = tag, annotation = tag)
        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
            append(stringResource(R.string.click_to_see_label))
        }
        pop()
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.hidden_content_description),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = annotatedString,
            onTextLayout = { layoutResult = it },
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { offset ->
                    layoutResult?.let { layout ->
                        val position = layout.getOffsetForPosition(offset)
                        annotatedString.getStringAnnotations(
                            tag = tag,
                            start = position,
                            end = position
                        ).firstOrNull()?.let { _ ->
                            handleClick()
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun CommentMask(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = stringResource(R.string.hidden_content_label),
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = stringResource(R.string.illegal_content_description),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
        )
    }
}

@Composable
@Preview
fun PreviewCommentMask() {
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CommentMask(
                status = UiStatus.ILLEGAL,
                isAccessible = false,
                modifier = Modifier.padding(16.dp)
            ) {}
            CommentMask(
                status = UiStatus.HIDDEN,
                isAccessible = false,
                modifier = Modifier.padding(16.dp)
            ) {}
        }
    }
}
