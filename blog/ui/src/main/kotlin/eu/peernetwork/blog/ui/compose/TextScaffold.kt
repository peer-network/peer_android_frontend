package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.core.ui.design.compose.DesignCard
import eu.peernetwork.core.ui.design.compose.DesignRichText
import eu.peernetwork.core.ui.design.compose.DesignTextButton
import eu.peernetwork.core.ui.design.compose.DesignTitleStyle
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun TextScaffold(
    author: UiAuthor,
    title: AnnotatedString,
    caption: String,
    description: AnnotatedString,
    modifier: Modifier = Modifier,
    onAuthorClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    engagements: @Composable RowScope.() -> Unit = {},
    moderation: @Composable RowScope.() -> Unit = {}
) {
    val updatedAction by rememberUpdatedState(actions)
    val updatedEngagements by rememberUpdatedState(engagements)
    val updatedModeration by rememberUpdatedState(moderation)
    val handleAuthorClick by rememberUpdatedState(onAuthorClick)
    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(24.dp)
        ) {
            DesignCard(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(32.dp),
                contentPadding = PaddingValues(16.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Row {
                    Icon(
                        painter = painterResource(R.drawable.ic_quote),
                        contentDescription = stringResource(R.string.post_description),
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(start = 12.dp)) {
                        DesignRichText(
                            text = title,
                            onClick = { handleAuthorClick(author.id) },
                            onMentionClick = onMentionClick,
                            onHashtagClick = onHashtagClick,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        DesignRichText(
                            text = description,
                            onClick = { handleAuthorClick(author.id) },
                            onMentionClick = onMentionClick,
                            onHashtagClick = onHashtagClick,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.tertiary
                            ),
                            modifier = Modifier.padding(top = 4.dp)
                                .defaultMinSize(minHeight = 48.dp)
                        )
                        Text(
                            text = "~ ${author.username}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.tertiary
                            ),
                            modifier = Modifier.align(Alignment.End)
                                .padding(top = 16.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
        Image(
            painter = painterResource(eu.peernetwork.core.ui.R.drawable.overlay_gradient),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row { updatedEngagements() }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AuthorView(
                    author,
                    caption,
                    size = 48.dp,
                    padding = PaddingValues(start = 12.dp),
                    onClick = { handleAuthorClick(author.id) },
                    modifier = Modifier.weight(1f),
                    style = DesignTitleStyle(
                        span = SpanStyle(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Normal,
                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
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
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTextScaffold() {
    PeerTheme {
        TextScaffold(
            author = UiAuthor(
                id = "",
                slug = 12034,
                username = "JohnDoe",
                imageUrl = "http://localhost",
                isfollowing = false,
                isfollowed = false
            ),
            caption = "2mins ago",
            title = buildAnnotatedString { append("Title...") },
            description = buildAnnotatedString { append("Description...") },
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
        )
    }
}
