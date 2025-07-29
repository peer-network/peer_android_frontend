package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.design.compose.DesignDetailLayout
import eu.peernetwork.core.ui.design.compose.DesignLead
import eu.peernetwork.core.ui.design.compose.DesignTitleStyle
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun AuthorView(
    author: UiAuthor,
    description: String,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    padding: PaddingValues = PaddingValues(start = 8.dp),
    color: Color = MaterialTheme.colorScheme.onBackground,
    descriptionColor: Color = MaterialTheme.colorScheme.tertiary,
    style: DesignTitleStyle = DesignTitleStyle(
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
    onClick: () -> Unit = {},
) {
    val handleClick by rememberUpdatedState(onClick)
    DesignDetailLayout(
        lead = {
            DesignAvatar {
                DesignAsyncImage(
                    label = author.username,
                    imageUrl = author.imageUrl,
                    size = size,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = color,
                    ),
                    modifier = Modifier.clickable(role = Role.Button, onClick = handleClick)
                )
            }
        },
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DesignLead(
            title = author.username,
            caption = author.slug.toString(),
            description = AnnotatedString(description),
            verticalArrangement = Arrangement.Center,
            spacer = {},
            modifier = Modifier.padding(padding),
            style = style,
        )
    }
}

@Preview
@Composable
fun PreviewAuthorBadge() {
    PeerTheme {
        AuthorView(
            author = UiAuthor(
                id = "",
                slug = 12034,
                username = "JohnDoe",
                imageUrl = "http://localhost",
                isfollowing = false,
                isfollowed = false
            ),
            description = ("2 mins ago")

        )
    }
}
