package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignTextButton
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DialogPostCard(
    author: UiAuthor,
    description: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    actions: @Composable RowScope.() -> Unit = {},
    engagements: @Composable RowScope.() -> Unit = {},
    caption: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    PostCard(
        modifier = modifier,
        header = {},
        toolbar = {},
        background = {},
        contentPadding = contentPadding,
        footer = {}
    ) {
        Box {
            Column(modifier = Modifier.navigationBarsPadding()) {
                content()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    engagements()
                    Spacer(modifier = Modifier.weight(1f))
                    DesignTextButton(
                        onClick = {},
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Box(modifier = Modifier.padding(horizontal = 24.dp)) { caption() }
            }
            Image(
                painter = painterResource(eu.peernetwork.blog.ui.R.drawable.overlay_gradient),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
                    .height(136.dp)
                    .rotate(180f),
                contentScale = ContentScale.FillWidth
            )
            Row(modifier = Modifier.padding(
                vertical = 16.dp,
                horizontal = 24.dp
            ).statusBarsPadding()) {
                AuthorBar(
                    author,
                    description,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSecondary,
                    descriptionColor = MaterialTheme.colorScheme.onSecondary
                )
                actions()
            }
        }
    }
}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDialogPostCard() {
    PeerTheme {
        DialogPostCard(
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
                    buildAnnotatedString { append("John Doe") },
                    buildAnnotatedString { append("Description...") },
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}
