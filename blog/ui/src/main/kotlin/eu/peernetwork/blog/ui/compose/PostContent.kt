package eu.peernetwork.blog.ui.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.core.renderer.ImageView

@Composable
fun PostContent(
    post: UiPost,
    position: Int,
    current: MutableState<Int>,
    enable: State<Boolean>,
    imageView: ImageView,
    audioPlayer: AudioPlayer
) {
    val media = remember { post.media.first() }
    imageView(
        Modifier,
        ImageView.Spec(media.path, post.aspectRatio)
    )
//    if (post.type == UiPost.Type.AUDIO) {
//        audioPlayer.Thumbnail(
//            path = post.media.first().path,
//            enable = enable,
//            position = position,
//            current = current,
//            modifier = Modifier
//        )
//    } else if (post.media.size > 1) {
//        val pagerState = rememberPagerState(initialPage = 0) { post.media.size }
//        PhotoPager(
//            pagerState,
//            post.aspectRatio,
//            post.media,
//            { PhotoIndicator(pagerState, post.media) }
//        ) { path ->
//            imageView(
//                Modifier,
//                ImageView.Spec(
//                    path,
//                    null,
//                    ContentScale.Crop,
//                    500f,
//                )
//            )
//            imageView(
//                Modifier,
//                ImageView.Spec(path, post.aspectRatio)
//            )
//        }
//    } else {
//    }
}
