package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun CommentUserMask(
    status: UiStatus,
    isAuthor: Boolean,
    isAccessible: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    description: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedDescription by rememberUpdatedState(description)
    val updatedContent by rememberUpdatedState(content)
    val isVisible = remember { mutableStateOf(!isAuthor) }
    Box(modifier = modifier) {
        if (status == UiStatus.ILLEGAL) {
            CommentUserMask(
                icon = painterResource(R.drawable.ic_delete),
                title = stringResource(R.string.illegal_username),
                onClick = onClick
            ) { updatedDescription() }
        } else if (!isAccessible) {
            if (isVisible.value) {
                updatedContent()
            } else {
                CommentUserMask(
                    icon = painterResource(R.drawable.ic_hidden),
                    title = stringResource(R.string.hidden_content_label),
                    onClick = { isVisible.value = true }
                ) { updatedDescription() }
            }
        } else {
            updatedContent()
        }
    }
}

@Composable
fun CommentUserMask(
    icon: Painter,
    title: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(10.dp)
                .clickable(onClick = onClick)
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(horizontal = 10.dp)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            updatedContent()
        }
    }
}

@Composable
@Preview
fun PreviewCommentUserMask() {
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CommentUserMask(
                status = UiStatus.ILLEGAL,
                isAuthor = false,
                onClick = {},
                isAccessible = false,
                description = {}
            ) {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            CommentUserMask(
                status = UiStatus.HIDDEN,
                isAuthor = false,
                onClick = {},
                isAccessible = false,
                description = {}
            ) {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
