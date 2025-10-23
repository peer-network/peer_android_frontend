package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.ui.design.material.DesignAsyncImage
import eu.peernetwork.core.ui.design.material.DesignAvatar
import eu.peernetwork.core.ui.design.material.DesignDetailLayout
import eu.peernetwork.core.ui.design.material.DesignRichText
import eu.peernetwork.core.ui.design.material.DesignTitleStyle
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun CommentSummary(
    model: UiContent,
    likes: Int,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    color: Color = MaterialTheme.colorScheme.onBackground,
    descriptionColor: Color = MaterialTheme.colorScheme.tertiary,
    titleOnClick: (() -> Unit)? = null,
    onComment: () -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
    content: @Composable () -> Unit
) {
    val handleAuthorClick by rememberUpdatedState(onAuthorClick)
    val updatedContent by rememberUpdatedState(content)
    DesignDetailLayout(
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        lead = {
            DesignAvatar {
                DesignAsyncImage(
                    label = model.author.username,
                    imageUrl = model.author.imageUrl,
                    size = 36.dp,
                    color = MaterialTheme.colorScheme.surfaceDim,
                    style = MaterialTheme.typography.bodyMedium.copy(color = color),
                    modifier = Modifier.clickable {
                        handleAuthorClick(model.author.id)
                    }
                )
            }
        },
        modifier = modifier
    ) {
        Row(modifier = Modifier.padding(start = 16.dp)) {
            Column(modifier = Modifier.weight(1f)
                .heightIn(max = 128.dp)
                .verticalScroll(rememberScrollState())) {
                val annotated = buildAnnotatedString {
                    val hashIndex = model.title.indexOf('#')
                    if (hashIndex != -1) {
                        append(model.title.substring(0, hashIndex))
                        withStyle(
                            style = SpanStyle(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Normal,
                                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                color = descriptionColor
                            )
                        ) {
                            append(model.title.substring(hashIndex))
                        }
                    } else {
                        append(model.title)
                    }
                }
                DesignRichText(
                    title = annotated,
                    description = model.description,
                    verticalArrangement = Arrangement.Center,
                    spacer = {},
                    maxLines = 1,
                    maxContentLines = 3,
                    titleOnClick = titleOnClick,
                    style = DesignTitleStyle(
                        span = SpanStyle(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Normal,
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                            color = descriptionColor
                        ),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = color,
                            fontWeight = FontWeight.SemiBold
                        ),
                        descriptionStyle = MaterialTheme.typography.bodySmall.copy(
                            color = descriptionColor
                        )
                    ),
                    onMentionClick = onMentionClick,
                    onHashtagClick = onHashtagClick
                )
                if (likes > 0) {
                    Text(
                        text = stringResource(if (likes > 1) {
                            R.string.likes_label
                        } else {
                            R.string.like_by_label
                        }, likes),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.surfaceTint,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.clickable(
                            onClick = onComment,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ).padding(bottom = 4.dp)
                    )
                }
            }
            updatedContent()
        }
    }
}

@Preview
@Composable
fun CommentSummaryPreview() {
    PeerTheme {
        CommentSummary(
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
                    append("This is a mock description for a content post. It's purely for testing.") },
                likes = 25,
                isLiked = true,
                isDisliked = false,
                isViewed = false,
                dislikes = 3,
                views = 2,
                comment = 5,
                url = ""
            ),
            likes = 2,
        ) { Text("Hello, world!") }
    }
}
