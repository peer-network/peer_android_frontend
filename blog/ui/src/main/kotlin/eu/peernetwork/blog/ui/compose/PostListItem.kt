package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.post.photo.formatTimeAgo
import java.util.TimeZone

@Composable
fun LazyItemScope.PostListItem(
    post: UiPost,
    position: Int,
    state: State<Long>,
    onClick: () -> Unit = {},
    userOnClick: () -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    engagements: @Composable RowScope.() -> Unit,
    moderation: @Composable RowScope.() -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (UiPost) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val localCreatedAtMillis = remember(post.createdAt) {
        post.createdAt + TimeZone.getDefault().getOffset(post.createdAt)
    }
    val timer = remember(state.value) {
        localCreatedAtMillis.formatTimeAgo(state.value)
    }
    Spacer(modifier = Modifier.height(
        if (position == 0 && post.type == UiPost.Type.TEXT) {
            12.dp
        } else {
            0.dp
        }
    ))
    if (post.type != UiPost.Type.TEXT) {
        MediaPostCard(
            author = post.author,
            description = timer,
            modifier = Modifier.padding(bottom = 16.dp),
            caption = {
                PostSummary(
                    post.author.username,
                    post.title,
                    post.description,
                    userOnClick = userOnClick,
                    onMentionClick = onMentionClick,
                    onHashtagClick = onHashtagClick
                )
            },
            engagements = engagements,
            moderation = moderation,
            onClick = onClick,
            actions = actions
        ) { updatedContent(post) }
    } else {
        TextPostCard(
            author = post.author,
            description = timer,
            modifier = Modifier.padding(bottom = 12.dp)
                .padding(horizontal = 8.dp),
            engagements = engagements,
            moderation = moderation,
            onClick = onClick,
            actions = actions
        ) {
            PostText(
                post.title,
                post.description,
                Modifier.padding(top = 12.dp, bottom = 4.dp),
                onMentionClick = onMentionClick,
                onHashtagClick = onHashtagClick
            )
        }
    }
}
