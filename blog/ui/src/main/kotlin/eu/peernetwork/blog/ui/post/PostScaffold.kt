package eu.peernetwork.blog.ui.post

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.mapper.v2.format
import eu.peernetwork.blog.ui.model.v2.UiEngagement
import eu.peernetwork.blog.ui.model.v2.UiPostDetail
import eu.peernetwork.blog.ui.model.v2.UiTimer
import eu.peernetwork.core.ui.design.luna.DesignBox
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostScaffold(
    model: UiPostDetail,
    pinnedBy: String? = null,
    onMenu: () -> Unit,
    onClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onContentClick: (DesignRichText, String) -> Unit,
    engagement: @Composable () -> Unit,
    connection: @Composable RowScope.() -> Unit,
) {
    PostScaffold(
        model = model,
        onMenu = onMenu,
        onClick = onClick,
        onAuthorClick = onAuthorClick,
        engagement = engagement,
        pinnedBy = pinnedBy,
        connection = connection
    ) {
        PostText(
            model = model,
            modifier = Modifier.heightIn(min = 36.dp),
            onClick = onContentClick
        )
    }
}

@Composable
fun PostScaffold(
    model: UiPostDetail,
    pinnedBy: String? = null,
    onMenu: () -> Unit,
    onClick: () -> Unit,
    onAuthorClick: () -> Unit,
    engagement: @Composable () -> Unit,
    connection: @Composable RowScope.() -> Unit,
    background: @Composable BoxScope.() -> Unit = { PostScaffoldBackground() },
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val updatedContent by rememberUpdatedState(content)
    DesignBox(background = background) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
                .padding(vertical = 7.dp)
                .clickable(onClick = onClick)
        ) {
            PostToolbar(
                slug = model.slug,
                username = model.username,
                imageUrl = model.imageUrl,
                pinnedBy = pinnedBy,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.padding(12.dp),
                onAuthorClick = onAuthorClick,
                onMenu = onMenu,
                connection = connection
            )
            updatedContent()
            PostStatus(
                time = context.format(model.time),
                engagement = engagement,
                modifier = Modifier.padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
fun BoxScope.PostScaffoldBackground(
    color: Color = MaterialTheme.colorScheme.surfaceDim
) {
    Box(modifier = Modifier.fillMaxSize()
        .padding(vertical = 7.dp)
        .padding(horizontal = 8.dp)
        .clip(shape = RoundedCornerShape(size = 24.dp))
        .background(color)
        .align(Alignment.Center))
}

@Composable
fun PostExpandedScaffold(
    model: UiPostDetail,
    pinnedBy: String? = null,
    onMenu: () -> Unit,
    onClick: () -> Unit,
    onAuthorClick: () -> Unit,
    engagement: @Composable () -> Unit,
    onContentClick: (DesignRichText, String) -> Unit,
    connection: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val updatedContent by rememberUpdatedState(content)
    Column {
        PostToolbar(
            slug = model.slug,
            username = model.username,
            imageUrl = model.imageUrl,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 10.dp),
            pinnedBy = pinnedBy,
            onAuthorClick = onAuthorClick,
            onMenu = onMenu,
            connection = connection
        )
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceDim)
                .clickable(onClick = onClick)
        ) { updatedContent() }
        PostStatus(
            username = model.username,
            pinnedBy = pinnedBy,
            title = model.title,
            description = model.description,
            time = context.format(model.time),
            engagement = engagement,
            modifier = Modifier
                .padding(horizontal = 16.dp),
            onClick = onContentClick
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewUserOption() {
    DesignTheme(isDarkMode = false) {
        val model = UiPostDetail(
            id = "#test",
            title = buildAnnotatedString { append("John Doe") },
            slug = "#12034",
            username = "JohnDoe",
            imageUrl = "http://localhost",
            description = buildAnnotatedString {
                append("This is a mock description for a content post. It's purely for testing.")
            },
            time = UiTimer.Date("Oct 20, 2023")
        )
        val engagement = UiEngagement(
            id = "<test-id>",
            likes = "5k",
            dislikes = "1k",
            isDisliked = false,
            isLiked = false,
            views = "3k",
            comment = "1k"
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(8.dp))
            PostScaffold(
                model = model,
                onMenu = {},
                onClick = {},
                onAuthorClick = {},
                engagement = { EngagementReaction(engagement) {} },
                onContentClick = { _,_ -> }
            ) {
                DesignButton(
                    minHeight = 32.dp,
                    onClick = { },
                    style = MaterialTheme.typography.labelMedium
                        .copy(fontWeight = FontWeight.Bold),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) { Text("peer") }
            }
            PostScaffold(
                model = model,
                onMenu = {},
                onClick = {},
                onAuthorClick = {},
                engagement = { EngagementReaction(engagement) {} },
                pinnedBy = "Thomas",
                connection = {
                    DesignButton(
                        minHeight = 32.dp,
                        onClick = {  },
                        style = MaterialTheme.typography.labelMedium
                            .copy(fontWeight = FontWeight.Bold),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) { Text("peer") }
                }
            ) {
                Box(modifier = Modifier.fillMaxWidth()
                    .height(56.dp))
            }
            PostExpandedScaffold(
                model = model,
                pinnedBy = "Thomas",
                onMenu = {},
                onClick = {},
                onAuthorClick = {},
                engagement = { EngagementReaction(engagement) {} },
                connection = {
                    DesignButton(
                        minHeight = 32.dp,
                        onClick = {  },
                        style = MaterialTheme.typography.labelMedium
                            .copy(fontWeight = FontWeight.Bold),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) { Text("peer") }
                },
                onContentClick = { _,_ -> }
            ) {
                Box(modifier = Modifier.fillMaxWidth()
                    .height(260.dp))
            }
        }
    }
}
