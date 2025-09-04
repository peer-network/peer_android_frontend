package eu.peernetwork.blog.ui.compose

import android.net.Uri
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiPost

@Composable
fun PostItem(
    post: UiPost,
    position: Int,
    onClick: () -> Unit = {},
    onAuthorClick: () -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    engagements: @Composable RowScope.() -> Unit,
    moderation: @Composable RowScope.() -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    audio: @Composable (UiPost) -> Unit,
    audioPreview: @Composable (UiPost) -> Unit,
    video: @Composable (UiPost) -> Unit,
    image: @Composable (UiPost) -> Unit
) {
    val updatedAudio by rememberUpdatedState(audio)
    val updatedAudioPreview by rememberUpdatedState(audioPreview)
    val updatedVideo by rememberUpdatedState(video)
    val updatedImage by rememberUpdatedState(image)
    when (post.type) {
        UiPost.Type.AUDIO -> {
            if (post.media.first().options.cover == null) {
                AudioView(
                    author = post.author,
                    description = post.time,
                    modifier = Modifier.padding(top = if (position == 0) {
                        12.dp
                    } else {
                        0.dp
                    }).padding(bottom = 12.dp)
                        .padding(horizontal = 8.dp),
                    onClick = onClick,
                    onAuthorClick = onAuthorClick,
                    engagements = engagements,
                    moderation = moderation,
                    actions = actions,
                    audio = { updatedAudio(post) }
                ) {
                    PostTitle(
                        title = post.title,
                        description = post.description,
                        Modifier.padding(top = 12.dp, bottom = 4.dp),
                        onMentionClick = onMentionClick,
                        onHashtagClick = onHashtagClick
                    )
                }
            } else {
                MediaView(
                    author = post.author,
                    description = post.time,
                    modifier = Modifier.padding(bottom = 16.dp),
                    caption = {
                        TextView(
                            username = post.author.username,
                            title = post.title,
                            description = post.description,
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
                ) { updatedAudioPreview(post) }
            }
        }
        UiPost.Type.TEXT -> {
            TextPreview(
                author = post.author,
                description = post.time,
                modifier = Modifier.padding(top = if (position == 0) {
                    12.dp
                } else {
                    0.dp
                }).padding(bottom = 12.dp)
                    .padding(horizontal = 8.dp),
                engagements = engagements,
                moderation = moderation,
                onClick = onClick,
                onAuthorClick = onAuthorClick,
                actions = actions
            ) {
                PostTitle(
                    title = post.title,
                    description = post.description,
                    Modifier.padding(top = 12.dp, bottom = 4.dp),
                    onMentionClick = onMentionClick,
                    onHashtagClick = onHashtagClick
                )
            }
        }
        UiPost.Type.IMAGE -> {
            MediaView(
                author = post.author,
                description = post.time,
                modifier = Modifier.padding(bottom = 16.dp),
                caption = {
                    TextView(
                        username = post.author.username,
                        title = post.title,
                        description = post.description,
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
            ) { updatedImage(post) }
        }
        UiPost.Type.VIDEO -> {
            MediaView(
                author = post.author,
                description = post.time,
                modifier = Modifier.padding(bottom = 16.dp),
                caption = {
                    TextView(
                        username = post.author.username,
                        title = post.title,
                        description = post.description,
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
            ) { updatedVideo(post) }
        }
    }
}
