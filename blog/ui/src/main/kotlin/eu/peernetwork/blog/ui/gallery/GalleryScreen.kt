package eu.peernetwork.blog.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import eu.peernetwork.blog.ui.engagement.EngagementInteractor.Companion.LocalEngagementInteractor
import eu.peernetwork.blog.ui.engagement.EngagementReaction.Companion.LocalEngagementReaction
import eu.peernetwork.blog.ui.engagement.EngagementReactionStream
import eu.peernetwork.blog.ui.mapper.v2.format
import eu.peernetwork.blog.ui.mapper.v2.query
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.model.v2.UiPostType
import eu.peernetwork.blog.ui.post.PostInteractor.Companion.LocalPostInteractor
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.blog.ui.post.PostNavigator.Companion.LocalPostNavigator
import eu.peernetwork.core.ui.design.material.DesignThumbnail
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoPlayer

@Composable
fun GalleryScreen(
    position: Int,
    selected: MutableIntState,
    showSheet: MutableState<UiPost?>,
    enabled: Boolean,
    post: UiPost,
    connection: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val navigator = LocalPostNavigator.current
    val reaction = LocalEngagementReaction.current
    val engagementInteractor = LocalEngagementInteractor.current
    val interactor = LocalPostInteractor.current
    val thumbnail = interactor.observe()
    val length = remember { mutableLongStateOf(0L) }
    val progress = remember { mutableFloatStateOf(0f) }
    EngagementReactionStream(
        post = post,
        state = engagementInteractor.observe()
    ) { engagement ->
        GalleryScaffold(
            slug = post.author.slug.toString(),
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
                navigator.navigate(PostNavigator.Route.Profile(post.author.id))
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding(),
            connection = connection
        ) { media ->
            if (post.type == UiPostType.VIDEO) {
                val path = "${media}${UiMimeType.Video.query()}"
                val bitmap = remember { derivedStateOf { thumbnail.value[path] } }
                DesignThumbnail(media, bitmap) {
                    interactor.background(
                        media = path,
                        aspectRatio = post.asset.ratio,
                        width = with(density) { maxWidth.roundToPx() },
                        height = with(density) { maxHeight.roundToPx() },
                        fit = true
                    )
                }
                interactor.component().videoPlayer()(
                    Modifier,
                    spec = VideoPlayer.Spec(
                        url = media,
                        ratio = post.asset.ratio,
                        progress = progress,
                        length = length,
                        enabled = enabled,
                    )
                )
            } else if (post.type == UiPostType.IMAGE) {
                interactor.component().imageView()(
                    Modifier,
                    spec = ImageView.Spec(
                        url = media,
                        ratio = null,
                        contentScale = ContentScale.Crop,
                        blur = 500f,
                    )
                )
                interactor.component().imageView()(
                    Modifier,
                    spec = ImageView.Spec(media, post.asset.ratio, zoomable = true)
                )
            } else if (post.type == UiPostType.AUDIO) {
                interactor.component().audioPlayer()(
                    Modifier,
                    spec = AudioPlayer.Spec(
                        path = media,
                        length = length,
                        modifier = Modifier,
                        progress = progress,
                        enabled = enabled,
                        current = selected,
                        position = position
                    )
                )
            }
        }
    }
}
