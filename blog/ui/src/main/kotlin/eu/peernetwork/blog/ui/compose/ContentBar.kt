package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.model.UiContent
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.design.compose.DesignDetailLayout
import eu.peernetwork.core.ui.design.compose.DesignRichTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleTextStyle
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun ContentBar(
    model: UiContent,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    color: Color = MaterialTheme.colorScheme.onBackground,
    descriptionColor: Color = MaterialTheme.colorScheme.tertiary,
    titleOnClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
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
                    style = MaterialTheme.typography.bodyMedium.copy(color = color)
                )
            }
        },
        modifier = modifier
    ) {
        Row(modifier = Modifier.padding(start = 16.dp)) {
            DesignRichTitle(
                title = model.title,
                description = model.description,
                verticalArrangement = Arrangement.Center,
                spacer = {},
                maxLines = 1,
                maxContentLines = 3,
                modifier = Modifier.weight(1f),
                titleOnClick = titleOnClick,
                style = DesignTitleTextStyle(
                    span = SpanStyle(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Normal,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        color = descriptionColor
                    ),
                    style = MaterialTheme.typography.headlineMedium.copy(color = color),
                    descriptionStyle = MaterialTheme.typography.bodySmall.copy(
                        color = descriptionColor
                    )
                ),
            )
            content()
        }
    }
}

@Preview
@Composable
fun PreviewContentBar() {
    PeerTheme {
        ContentBar(
            model = UiContent(
                id = "abc123",
                title = "John Doe",
                author = UiAuthor(
                    id = "",
                    slug = 12034,
                    username = "JohnDoe",
                    imageUrl = "http://localhost",
                    isfollowing = false,
                    isfollowed = false
                ),
                createdAt = System.currentTimeMillis(),
                description = "This is a mock description for a content post. It's purely for testing.",
                likes = 25,
                isLiked = true,
                isDisliked = false,
                dislikes = 3,
                comment = 5
            ),
        ) { Text("Hello, world!") }
    }
}
