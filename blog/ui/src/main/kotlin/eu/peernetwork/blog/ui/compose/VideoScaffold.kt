package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignRichText
import eu.peernetwork.core.ui.design.compose.DesignTextButton
import eu.peernetwork.core.ui.design.compose.DesignTitleStyle
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun VideoScaffold(
    author: UiAuthor,
    description: AnnotatedString,
    modifier: Modifier = Modifier,
    onAuthorClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    engagements: @Composable RowScope.() -> Unit = {},
    moderation: @Composable RowScope.() -> Unit = {},
    caption: @Composable () -> Unit = {},
    progress: @Composable () -> Unit = {},
    header: @Composable () -> Unit = {},
    background: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val updatedAction by rememberUpdatedState(actions)
    val updatedContent by rememberUpdatedState(content)
    val updatedCaption by rememberUpdatedState(caption)
    val updatedHeader by rememberUpdatedState(header)
    val updatedBackground by rememberUpdatedState(background)
    val updatedEngagements by rememberUpdatedState(engagements)
    val updatedModeration by rememberUpdatedState(moderation)
    val updatedProgress by rememberUpdatedState(progress)
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
            .then(modifier)
    ) {
        updatedBackground()
        updatedContent()
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.overlay_gradient),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(180f)
                    .weight(.2f),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(R.drawable.overlay_gradient),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.8f),
                contentScale = ContentScale.Crop
            )
        }
        Column {
            updatedHeader()
            Spacer(modifier = Modifier.weight(1f)
                .padding(bottom = 16.dp))
            Column(horizontalAlignment = Alignment.End) {
                Row(modifier = Modifier.padding(horizontal = 24.dp)) { updatedEngagements() }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AuthorView(
                        author,
                        description,
                        size = 48.dp,
                        padding = PaddingValues(start = 12.dp),
                        onClick = onAuthorClick,
                        modifier = Modifier.weight(1f),
                        style = DesignTitleStyle(
                            span = SpanStyle(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Normal,
                                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            descriptionStyle = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    )
                    updatedAction()
                    Spacer(modifier.width(8.dp))
                    updatedModeration()
                }
                Spacer(modifier.height(4.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(start = 48.dp)
                    .padding(horizontal = 12.dp)) {
                    updatedCaption()
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Box(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .navigationBarsPadding()
                    .fillMaxWidth()
            ) { updatedProgress() }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewMediaPage() {
    PeerTheme {
        VideoScaffold(
            author = UiAuthor(
                id = "",
                slug = 12034,
                username = "JohnDoe",
                imageUrl = "http://localhost",
                isfollowing = false,
                isfollowed = false
            ),
            description = AnnotatedString("Description..."),
            engagements = {
                Column {
                    UiAction.ENGAGEMENTS.forEach {
                        DesignTextButton(
                            onClick = {},
                            contentPadding = PaddingValues(2.dp)
                        ) {
                            Column (horizontalAlignment = Alignment.CenterHorizontally,) {
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
            },
            caption = {
                DesignRichText(
                    buildAnnotatedString { append("John Doe") },
                    buildAnnotatedString { append("Description...") },
                    maxLines = 1,
                    maxContentLines = 1,
                    style = DesignTitleStyle(
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        descriptionStyle = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.tertiary
                        ),
                        span = SpanStyle(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Normal,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = MaterialTheme.colorScheme.tertiary
                        ),
                    ),
                    onMentionClick = { },
                    onHashtagClick = { }
                )
            },
            background = {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}
