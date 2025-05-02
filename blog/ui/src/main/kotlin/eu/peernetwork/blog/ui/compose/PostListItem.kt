package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.post.photo.formatTimeAgo

@Composable
fun LazyItemScope.PostListItem(
    post: UiPost,
    position: Int,
    state: State<Long>,
    onClick: () -> Unit = {},
    engagements: @Composable RowScope.() -> Unit,
    moderation: @Composable RowScope.() -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (UiPost) -> Unit
) {
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
            description = post.createdAt.formatTimeAgo(state.value),
            modifier = Modifier.padding(bottom = 16.dp),
            caption = {
                PostSummary(post.author.username, post.title, post.description)
            },
            engagements = engagements,
            moderation = moderation,
            onClick = onClick,
            actions = actions
        ) { content(post) }
    } else {
        TextPostCard(
            author = post.author,
            description = post.createdAt.formatTimeAgo(state.value),
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
                Modifier.padding(top = 12.dp, bottom = 4.dp)
            )
        }
    }
}
