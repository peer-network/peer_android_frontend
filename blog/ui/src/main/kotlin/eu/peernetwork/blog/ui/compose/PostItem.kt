package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiPost

@Composable
fun LazyItemScope.PostItem(
    post: UiPost,
    position: Int,
    onClick: () -> Unit = {},
    onAuthorClick: () -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    engagements: @Composable RowScope.() -> Unit,
    moderation: @Composable RowScope.() -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (UiPost) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Spacer(modifier = Modifier.height(
        if (position == 0 && post.type == UiPost.Type.TEXT) {
            12.dp
        } else {
            0.dp
        }
    ))
    if (post.type != UiPost.Type.TEXT) {
        MediaView(
            author = post.author,
            description = post.time,
            modifier = Modifier
                .padding(bottom = 16.dp),
            caption = {
                TextView(
                    post.author.username,
                    post.title,
                    post.description,
                    onAuthorClick = onAuthorClick,
                    onMentionClick = onMentionClick,
                    onHashtagClick = onHashtagClick
                )
            },
            engagements = engagements,
            moderation = moderation,
            onClick = onClick,
            onAuthorClick = onAuthorClick,
            actions = actions
        ) { updatedContent(post) }
    } else {
        TextPreview(
            author = post.author,
            description = post.time,
            modifier = Modifier.padding(bottom = 12.dp)
                .padding(horizontal = 8.dp),
            engagements = engagements,
            moderation = moderation,
            onClick = onClick,
            onAuthorClick = onAuthorClick,
            actions = actions
        ) {
            PostTitle(
                post.title,
                post.description,
                Modifier.padding(top = 12.dp, bottom = 4.dp),
                onMentionClick = onMentionClick,
                onHashtagClick = onHashtagClick
            )
        }
    }
}
