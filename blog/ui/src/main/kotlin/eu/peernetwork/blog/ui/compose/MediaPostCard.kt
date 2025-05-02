package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignTextButton
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun MediaPostCard(
    author: UiAuthor,
    description: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    engagements: @Composable RowScope.() -> Unit = {},
    moderation: @Composable RowScope.() -> Unit = {},
    caption: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    PostCard(
        modifier = modifier,
        header = {},
        toolbar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
                    .padding(horizontal = 24.dp)
            ) {
                engagements()
                Spacer(modifier = Modifier.weight(1f))
                moderation()
            }
            Box(modifier = Modifier.padding(horizontal = 24.dp)) { caption() }
        },
        background = {},
        contentPadding = contentPadding,
        footer = { }
    ) {
        Box {
            content()
            Image(
                painter = painterResource(eu.peernetwork.blog.ui.R.drawable.overlay_gradient),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
                    .height(120.dp)
                    .rotate(180f),
                contentScale = ContentScale.FillWidth
            )
            Row(modifier = Modifier.padding(
                vertical = 16.dp,
                horizontal = 24.dp
            )) {
                AuthorBar(
                    author,
                    description,
                    onClick = onClick,
                    modifier = Modifier.weight(1f),
                    descriptionColor = MaterialTheme.colorScheme.onBackground
                )
                actions()
            }
        }
    }
}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewImagePostCard() {
    PeerTheme {
        MediaPostCard(
            modifier = Modifier.padding(bottom = 24.dp),
            author = UiAuthor(
                id = "",
                slug = 12034,
                username = "JohnDoe",
                imageUrl = "http://localhost",
                isfollowing = false,
                isfollowed = false
            ),
            description = "Description...",
            engagements = {
                UiAction.ENGAGEMENTS.forEach {
                    DesignTextButton(
                        onClick = {},
                        contentPadding = PaddingValues(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = it.id),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(32.dp)
                            )
                            Text("0", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            },
            caption = {
                PostSummary(
                    "JohnDoe",
                    "JohnDoe",
                    "Description..."
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}
