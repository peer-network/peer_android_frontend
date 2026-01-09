package eu.peernetwork.blog.ui.gallery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import eu.peernetwork.blog.ui.extension.route
import eu.peernetwork.blog.ui.mapper.query
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiPostType
import eu.peernetwork.blog.ui.post.PostInteractor.Companion.LocalPostInteractor
import eu.peernetwork.blog.ui.post.PostMask
import eu.peernetwork.blog.ui.post.PostNavigator.Companion.LocalPostNavigator
import eu.peernetwork.core.ui.design.material.DesignThumbnail
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.MediaController
import eu.peernetwork.media.core.renderer.VideoPlayer

@Composable
fun BoxWithConstraintsScope.GalleryMedia(
    post: UiPost,
    media: UiMedia,
    position: Int,
    isAdmin: Boolean,
    hasMedia: Boolean,
    isAccessible: Boolean,
    enabled: State<Boolean>,
    progress: MutableFloatState,
) {
    val density = LocalDensity.current
    val interactor = LocalPostInteractor.current
    val navigator = LocalPostNavigator.current
    val thumbnail = interactor.observe()
    val length = remember { mutableLongStateOf(0L) }
    if (post.type == UiPostType.VIDEO) {
        val path = "${media.path}${UiMimeType.Video.query()}"
        val bitmap = remember { derivedStateOf { thumbnail.value[path] } }
        DesignThumbnail(media.path, bitmap) {
            interactor.background(
                media = path,
                aspectRatio = post.asset.ratio,
                width = with(density) { maxWidth.roundToPx() },
                height = with(density) { maxHeight.roundToPx() },
                fit = true
            )
        }
        PostMask(
            status = post.status,
            isAuthor = isAdmin,
            isAccessible = isAccessible
        ) {
            interactor.component().videoPlayer()(
                Modifier,
                spec = VideoPlayer.Spec(
                    url = media.path,
                    ratio = post.asset.ratio,
                    progress = progress,
                    length = length,
                    enabled = enabled.value,
                )
            )
        }
    } else if (post.type == UiPostType.IMAGE) {
        interactor.component().imageView()(
            Modifier,
            spec = ImageView.Spec(
                url = media.path,
                ratio = null,
                contentScale = ContentScale.Crop,
                blur = 500f,
            )
        )
        PostMask(
            status = post.status,
            isAuthor = isAdmin,
            isAccessible = isAccessible
        ) {
            interactor.component().imageView()(
                Modifier,
                spec = ImageView.Spec(
                    url = media.path,
                    ratio = post.asset.ratio,
                    zoomable = true
                )
            )
        }
    } else if (post.type == UiPostType.AUDIO) {
        PostMask(
            status = post.status,
            isAuthor = isAdmin,
            isAccessible = isAccessible
        ) {
            interactor.component().audioPlayer()(
                Modifier,
                spec = AudioPlayer.Spec(
                    path = media.path,
                    cover = media.display.cover,
                    length = length,
                    modifier = Modifier,
                    ratio = post.asset.ratio,
                    progress = progress,
                    enabled = enabled,
                    position = position
                )
            )
        }
    } else {
        PostMask(
            status = post.status,
            isAuthor = isAdmin,
            isAccessible = isAccessible
        ) {
            GalleryCaption(
                description = post.description,
                modifier = Modifier.fillMaxSize()
            ) { spec, value -> navigator.navigate(spec.route(value)) }
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (post.type != UiPostType.TEXT
            && post.type != UiPostType.IMAGE
            && post.type != UiPostType.VIDEO) {
            interactor.component().mediaController()(
                Modifier.fillMaxWidth(),
                spec = MediaController.Spec(
                    path = media.path,
                    translucent = hasMedia,
                )
            )
        }
    }
}
