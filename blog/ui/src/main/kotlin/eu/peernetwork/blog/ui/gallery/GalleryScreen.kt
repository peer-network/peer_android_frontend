package eu.peernetwork.blog.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.engagement.EngagementInteractor.Companion.LocalEngagementInteractor
import eu.peernetwork.blog.ui.engagement.EngagementReaction.Companion.LocalEngagementReaction
import eu.peernetwork.blog.ui.engagement.EngagementReactionStream
import eu.peernetwork.blog.ui.extension.route
import eu.peernetwork.blog.ui.mapper.format
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiPostType
import eu.peernetwork.blog.ui.post.PostInteractor.Companion.LocalPostInteractor
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.blog.ui.post.PostNavigator.Companion.LocalPostNavigator

@Composable
fun GalleryScreen(
    uuid: String,
    position: Int,
    enabled: State<Boolean>,
    showSheet: MutableState<UiPost?>,
    post: UiPost,
    connection: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val navigator = LocalPostNavigator.current
    val reaction = LocalEngagementReaction.current
    val engagementInteractor = LocalEngagementInteractor.current
    val interactor = LocalPostInteractor.current
    val progress = remember { mutableFloatStateOf(0f) }
    EngagementReactionStream(
        post = post,
        state = engagementInteractor.observe()
    ) { engagement ->
        GalleryScaffold(
            slug = post.author.slug.toString(),
            type = post.type,
            username = post.author.username,
            title = post.title,
            description = post.description,
            imageUrl = post.author.imageUrl,
            time = context.format(post.time),
            asset = post.asset,
            engagement = engagement,
            onEngage = { reaction(post, it) },
            onMenu = { showSheet.value = post },
            showAuthor = {
                navigator.navigate(
                    route = PostNavigator.Route.Profile(post.author.id)
                )
            },
            onContentClick = { spec, value -> navigator.navigate(spec.route(value)) },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding(),
            connection = connection,
            bottom = {
                if (post.type != UiPostType.TEXT
                    && post.type != UiPostType.IMAGE) {
                    interactor.component().mediaController().Progress(
                        progress = progress,
                        isPlaying = enabled,
                        modifier = Modifier.fillMaxWidth()
                            .height(height = 2.dp)
                    )
                }
            },
            menu = {
                if (post.type != UiPostType.TEXT
                    && post.type != UiPostType.IMAGE) {
                    interactor.component().mediaController().Volume()
                }
            }
        ) { media, hasMedia ->
            val isAuthor = uuid == post.author.id
            val isVisible = remember { mutableStateOf(!post.isAccessible || isAuthor) }
            GalleryMedia(
                media = media,
                post = post,
                position = position,
                isAdmin = isAuthor,
                hasMedia = hasMedia,
                isAccessible = post.isAccessible,
                enabled = enabled,
                isVisible = isVisible,
                progress = progress
            )
        }
    }
}
