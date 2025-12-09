package eu.peernetwork.blog.ui.comment

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.design.luna.DesignText
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppDarkRed

@Composable
fun CommentItem(
    slug: String,
    username: String,
    imageUrl: String,
    comment: AnnotatedString,
    isLiked: Boolean,
    likes: Int,
    color: Color = MaterialTheme.colorScheme.outline,
    onReply: () -> Unit,
    onViewLikes: () -> Unit,
    onLike: () -> Unit,
) {
    val handleOnLike by rememberUpdatedState(onLike)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
    ) {
        DesignAvatar {
            DesignImage(
                label = username,
                imageUrl = imageUrl,
                size = 36.dp,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 36.dp)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    append(username)
                    withStyle(SpanStyle(
                        color = MaterialTheme.colorScheme.outline,
                        fontStyle = FontStyle.Italic,
                        fontSize = MaterialTheme.typography.labelMedium.fontSize
                    )) { append(" #$slug") }
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                modifier = Modifier.clickable(onClick = onReply)
            )
            DesignText(
                text = comment,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
            )
            if (likes > 0 || isLiked) {
                Text(
                    text = if (isLiked && likes > 1) {
                        stringResource(R.string.likes_by_you_label, likes - 1)
                    } else if (likes > 1) {
                        stringResource(R.string.likes_label, likes)
                    } else if (isLiked) {
                        stringResource(R.string.liked_by_you_label)
                    } else {
                        stringResource(R.string.like_by_label)
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.clickable(onClick = onViewLikes)
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(if (likes > 0 || isLiked) {
                    R.drawable.ic_love
                } else {
                    R.drawable.ic_love_outline
                }),
                contentDescription = stringResource(R.string.like_label),
                tint = if (isLiked) {
                    PeerAppDarkRed
                } else {
                    color
                },
                modifier = Modifier.size(18.dp)
                    .then(if (!isLiked) {
                        Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { handleOnLike() }
                            )
                        }
                    } else {
                        Modifier
                    })
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewCommentItem() {
    DesignTheme {
        CommentItem(
            slug = "239100",
            username = "John",
            imageUrl = "http://localhost",
            comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum accumsan elementum commodo.".annotate(),
            isLiked = true,
            likes = 1,
            onReply = {},
            onViewLikes = {}
        ) {}
    }
}
