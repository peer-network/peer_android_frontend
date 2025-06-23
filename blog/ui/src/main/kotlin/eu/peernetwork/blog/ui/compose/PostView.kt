package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.core.ui.design.compose.DesignTextButton
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun TextPreview(
    author: UiAuthor,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onAuthorClick: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(
        top = 16.dp,
        start = 16.dp,
        end = 16.dp,
        bottom = 8.dp
    ),
    actions: @Composable RowScope.() -> Unit = {},
    engagements: @Composable RowScope.() -> Unit = {},
    moderation: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    val updatedAction by rememberUpdatedState(actions)
    val updatedContent by rememberUpdatedState(content)
    val updatedEngagements by rememberUpdatedState(engagements)
    val updatedModeration by rememberUpdatedState(moderation)
    PostScaffold(
        modifier = modifier,
        header = {
            Row {
                AuthorView(
                    author,
                    description,
                    modifier = Modifier.weight(1f),
                    onClick = onAuthorClick
                )
                updatedAction()
            }
        },
        toolbar = {},
        background = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(24.dp)
                    ).clickable(
                        enabled = true,
                        role = Role.Button,
                        onClick = onClick
                    )
            ) },
        contentPadding = contentPadding,
        footer = {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                updatedEngagements()
                Spacer(modifier = Modifier.weight(1f))
                updatedModeration()
            }
        }
    ) { updatedContent() }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTextPostCard() {
    PeerTheme {
        TextPreview(
            modifier = Modifier.padding(horizontal = 8.dp),
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
            }
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp))
        }
    }
}
