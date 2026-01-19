package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import eu.peernetwork.blog.ui.mapper.format
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.blog.ui.model.UiPostDetail
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.blog.ui.model.UiTimer
import eu.peernetwork.core.ui.design.luna.DesignBox
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostScaffold(
    model: UiPostDetail,
    isAuthor: Boolean,
    onClick: () -> Unit,
    onContentClick: (DesignRichText, String) -> Unit,
    toolbar: @Composable () -> Unit,
    engagement: @Composable () -> Unit,
) {
    PostScaffold(
        model = model,
        isAuthor = isAuthor,
        onClick = onClick,
        toolbar = toolbar,
        engagement = engagement
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
    isAuthor: Boolean = false,
    onClick: () -> Unit,
    toolbar: @Composable () -> Unit,
    engagement: @Composable () -> Unit,
    background: @Composable BoxScope.() -> Unit = { PostScaffoldBackground() },
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val updatedToolbar by rememberUpdatedState(toolbar)
    val updatedContent by rememberUpdatedState(content)
    DesignBox(background = background) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(vertical = 5.dp)
                .clickable(onClick = onClick)
        ) {
            updatedToolbar()
            updatedContent()
            PostStatus(
                time = context.format(model.time),
                content = engagement,
                reported = model.reported,
                isAuthor = isAuthor,
                isAccessible = model.isAccessible,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
fun BoxScope.PostScaffoldBackground(
    color: Color = MaterialTheme.colorScheme.surfaceDim
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 7.dp)
        .padding(horizontal = 8.dp)
        .clip(shape = RoundedCornerShape(size = 24.dp))
        .background(color)
        .align(Alignment.Center))
}

@Composable
fun PostExpandedScaffold(
    model: UiPostDetail,
    isAuthor: Boolean,
    onClick: () -> Unit,
    isVisible: State<Boolean>,
    toolbar: @Composable () -> Unit,
    engagement: @Composable () -> Unit,
    onAuthorClick: () -> Unit,
    onContentClick: (DesignRichText, String) -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val updatedToolbar by rememberUpdatedState(toolbar)
    val updatedContent by rememberUpdatedState(content)
    Column {
        updatedToolbar()
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceDim)
                .clickable(onClick = onClick)
        ) { updatedContent() }
        PostStatus(
            username = model.username,
            reported = model.reported,
            title = model.title,
            isAuthor = isAuthor,
            isVisible = isVisible.value,
            isAccessible = model.isAccessible,
            description = model.description,
            time = context.format(model.time),
            engagement = engagement,
            onAuthorClick = onAuthorClick,
            modifier = Modifier
                .padding(horizontal = 16.dp),
            onClick = onContentClick
        )
    }
}

@Composable
@Preview
fun PreviewPostScaffold() {
    DesignTheme(isDarkMode = true) {
        val model = UiPostDetail(
            id = "#test",
            uuid = "#test",
            title = buildAnnotatedString { append("John Doe") },
            slug = "#12034",
            username = "JohnDoe",
            imageUrl = "http://localhost",
            description = buildAnnotatedString {
                append("This is a mock description for a content post. It's purely for testing.")
            },
            time = UiTimer.Date("Oct 20, 2023"),
            reported = true,
            isAccessible = true,
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
        val isVisible = remember { mutableStateOf(true) }
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(8.dp))
            PostScaffold(
                model = model,
                isAuthor = true,
                onClick = {},
                toolbar = {
                    PostToolbar(
                        slug = model.slug,
                        status = UiStatus.VISIBLE,
                        isAccessible = true,
                        isAuthor = false,
                        username = model.username,
                        imageUrl = model.imageUrl,
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        modifier = Modifier.padding(10.dp),
                        onAuthorClick = {  },
                        onMenu = {  },
                        connection = {
                            DesignButton(
                                minHeight = 32.dp,
                                onClick = { },
                                style = MaterialTheme.typography.labelMedium
                                    .copy(fontWeight = FontWeight.Bold),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) { Text("peer") }
                        }
                    )
                },
                engagement = { EngagementReaction(engagement) {} },
                onContentClick = { _,_ -> }
            )
            PostScaffold(
                model = model,
                onClick = {},
                isAuthor = true,
                engagement = { EngagementReaction(engagement) {} },
                toolbar = {
                    PostToolbar(
                        slug = model.slug,
                        status = UiStatus.VISIBLE,
                        isAccessible = true,
                        isAuthor = false,
                        username = model.username,
                        imageUrl = model.imageUrl,
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        modifier = Modifier.padding(10.dp),
                        onAuthorClick = {  },
                        onMenu = {  },
                        connection = {
                            DesignButton(
                                minHeight = 32.dp,
                                onClick = { },
                                style = MaterialTheme.typography.labelMedium
                                    .copy(fontWeight = FontWeight.Bold),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) { Text("peer") }
                        }
                    )
                }
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp))
            }
            PostExpandedScaffold(
                model = model.copy(isAccessible = false),
                onClick = {},
                onAuthorClick = {},
                isVisible = isVisible,
                isAuthor = true,
                engagement = { EngagementReaction(engagement) {} },
                toolbar = {
                    PostToolbar(
                        slug = model.slug,
                        status = UiStatus.VISIBLE,
                        isAccessible = true,
                        isAuthor = false,
                        username = model.username,
                        imageUrl = model.imageUrl,
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        modifier = Modifier.padding(10.dp),
                        onAuthorClick = {  },
                        onMenu = {  },
                        connection = {
                            DesignButton(
                                minHeight = 32.dp,
                                onClick = { },
                                style = MaterialTheme.typography.labelMedium
                                    .copy(fontWeight = FontWeight.Bold),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) { Text("peer") }
                        }
                    )
                },
                onContentClick = { _,_ -> }
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp))
            }
        }
    }
}
