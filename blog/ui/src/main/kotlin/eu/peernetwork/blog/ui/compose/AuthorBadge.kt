package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.design.compose.DesignDetailLayout
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleTextStyle
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun AuthorBadge(
    author: UiAuthor,
    description: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    descriptionColor: Color = MaterialTheme.colorScheme.tertiary
) {
    DesignDetailLayout(
        lead = {
            DesignAvatar {
                DesignAsyncImage(
                    label = author.username,
                    imageUrl = author.imageUrl,
                    size = 36.dp,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = color,
                    )
                )
            }
        },
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DesignTitle(
            title = author.username,
            caption = author.slug.toString(),
            description = description,
            verticalArrangement = Arrangement.Center,
            spacer = {},
            modifier = Modifier.padding(start = 8.dp),
            style = DesignTitleTextStyle(
                span = SpanStyle(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    color = descriptionColor
                ),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                ),
                descriptionStyle = MaterialTheme.typography.labelSmall.copy(
                    color = descriptionColor
                )
            ),
        )
    }
}

@Preview
@Composable
fun PreviewAuthorBadge() {
    PeerTheme {
        AuthorBadge(
            author = UiAuthor(
                id = "",
                slug = 12034,
                username = "JohnDoe",
                imageUrl = "http://localhost"
            ),
            description = "2 mins ago"
        )
    }
}
