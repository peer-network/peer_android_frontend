package eu.peernetwork.blog.ui.post

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.ui.design.luna.DesignAnnotatedText
import eu.peernetwork.core.ui.design.luna.DesignBox
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostScaffold(
    model: UiPost.Detail,
    engagement: UiPost.Engagement,
    onPin: (() -> Unit)? = null,
    pinnedBy: String? = null,
    connection: @Composable () -> Unit,
) {
    PostScaffold(
        model = model,
        engagement = engagement,
        pinnedBy = pinnedBy,
        onPin = onPin,
        connection = connection
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp)
            .padding(bottom = 16.dp)
            .padding(top = 6.dp)) {
            DesignAnnotatedText(
                text = model.title,
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium,
            )
            DesignAnnotatedText(
                text = model.description,
                maxLines = 6,
                lineHeight = 18.sp,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun PostScaffold(
    model: UiPost.Detail,
    engagement: UiPost.Engagement,
    pinnedBy: String? = null,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    onPin: (() -> Unit)? = null,
    connection: @Composable () -> Unit = {},
    background: @Composable BoxScope.() -> Unit = { PostScaffoldBackground(color) },
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignBox(background = background) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp)
                .padding(vertical = 6.dp)
        ) {
            PostHeader(
                slug = model.slug,
                username = model.username,
                imageUrl = model.imageUrl,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(vertical = 10.dp),
                onAuthorClick = {},
                onPin = onPin,
                connection = connection
            )
            updatedContent()
            if (pinnedBy == null) {
                PostFooter(
                    time = model.time,
                    engagement = engagement,
                    modifier = Modifier.padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp)
                )
            } else {
                PostFooter(
                    time = model.time,
                    engagement = engagement,
                    pinnedBy = pinnedBy,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@Composable
fun BoxScope.PostScaffoldBackground(color: Color = MaterialTheme.colorScheme.surfaceContainerLowest) {
    Box(modifier = Modifier.fillMaxSize()
        .padding(vertical = 6.dp)
        .padding(horizontal = 12.dp)
        .clip(shape = RoundedCornerShape(size = 24.dp))
        .background(color)
        .align(Alignment.Center))
}

@Composable
fun PostScaffold(
    model: UiPost.Detail,
    engagement: UiPost.Engagement,
    pinnedBy: String? = null,
    connection: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Column {
        PostHeader(
            slug = model.slug,
            username = model.username,
            imageUrl = model.imageUrl,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(vertical = 8.dp),
            onAuthorClick = {},
            connection = connection
        )
        updatedContent()
        PostFooter(
            username = model.username,
            pinnedBy = pinnedBy,
            title = model.title,
            description = model.description,
            time = model.time,
            engagement = engagement,
            modifier = Modifier
                .padding(horizontal = 24.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewUserOption() {
    DesignTheme(isDarkMode = false) {
        val model = UiPost.Detail(
            title = buildAnnotatedString { append("John Doe") },
            slug = "#12034",
            username = "JohnDoe",
            imageUrl = "http://localhost",
            description = buildAnnotatedString {
                append("This is a mock description for a content post. It's purely for testing.")
            },
            time = "2h ago"
        )
        val engagement = UiPost.Engagement(
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
                engagement = engagement,
            ) {
                DesignButton(
                    minHeight = 32.dp,
                    onClick = { },
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) { Text("peer") }
            }
            PostScaffold(
                model = model,
                engagement = engagement,
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                onPin = {},
                pinnedBy = "Thomas",
                connection = {
                    DesignButton(
                        minHeight = 32.dp,
                        onClick = {  },
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) { Text("peer") }
                }
            ) {
                Box(modifier = Modifier.fillMaxWidth()
                    .height(64.dp))
            }
            PostScaffold(
                model = model,
                engagement = engagement,
                pinnedBy = "Thomas",
                connection = {
                    DesignButton(
                        minHeight = 32.dp,
                        onClick = {  },
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) { Text("peer") }
                }
            ) {
                Box(modifier = Modifier.fillMaxWidth()
                    .height(260.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest))
            }
        }
    }
}
