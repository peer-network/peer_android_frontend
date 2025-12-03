package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import eu.peernetwork.blog.ui.engagement.EngagementInteractor.Companion.LocalEngagementInteractor
import eu.peernetwork.blog.ui.mapper.v2.query
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.model.v2.UiPostType
import eu.peernetwork.blog.ui.post.PostInteractor.Companion.LocalPostInteractor
import eu.peernetwork.core.ui.design.material.DesignThumbnail
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoPlayer

@Composable
fun PostModal(
    position: Int,
    current: MutableIntState,
    enabled: Boolean,
    post: UiPost
) {
    val density = LocalDensity.current
    val engagement = LocalEngagementInteractor.current
    val interactor = LocalPostInteractor.current
    val thumbnail = interactor.observe()
    val length = remember { mutableLongStateOf(0L) }
    val progress = remember { mutableFloatStateOf(0f) }
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        PostModal(post) { media ->
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
                        current = current,
                        position = position
                    )
                )
            }
        }
    }
}

@Composable
fun PostModal(
    post: UiPost,
    content: @Composable BoxWithConstraintsScope.(String) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    if (post.asset.media.size == 1) {
        val path by remember { derivedStateOf { post.asset.media.first().path } }
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) { updatedContent(path) }
    } else {
        val pagerState = rememberPagerState(initialPage = 0) { post.asset.media.size }
        PostPager(
            pagerState,
            post.asset,
        ) { BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) { updatedContent(it) } }
    }
}
