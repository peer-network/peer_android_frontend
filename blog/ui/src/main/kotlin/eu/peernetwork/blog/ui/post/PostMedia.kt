package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.model.UiPost.Type
import eu.peernetwork.core.ui.design.material.DesignThumbnail
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoThumbnail

@Composable
fun PostMedia(
    type: Type,
    path: String,
    avatar: String,
    position: Int,
    aspectRatio: Float,
    status: State<Boolean>,
    enable: State<Boolean>,
    isActive: State<Boolean>,
    component: Post.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val configuration = LocalConfiguration.current
    val viewModel = viewModel(
        modelClass = PostViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle()
    if (type == Type.IMAGE) {
        component.imageView()(
            modifier = Modifier,
            spec = ImageView.Spec(
                url = path,
                ratio = aspectRatio,
                contentScale = ContentScale.Crop,
                blur = 500f,
            )
        )
        component.imageView()(
            modifier = Modifier,
            spec = ImageView.Spec(
                url = path,
                ratio = aspectRatio
            )
        )
    } else if (type == Type.VIDEO) {
        val isPlaying = remember { derivedStateOf { status.value && isActive.value } }
        val postThumbnail = remember { derivedStateOf { thumbnail.value[path] } }
        DesignThumbnail(
            enable = enable,
            thumbnail = path,
            bitmap = postThumbnail,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        ) {
            viewModel.videoBackground(
                media = path,
                aspectRatio = aspectRatio,
                width = configuration.screenWidthDp,
                height = (configuration.screenWidthDp / aspectRatio).toInt()
            )
        }
        component.videoThumbnail()(
            Modifier,
            spec = VideoThumbnail.Spec(
                url = path,
                ratio = aspectRatio,
                isPlaying = isPlaying
            )
        )
    } else if (type == Type.AUDIO) {
        Box(contentAlignment = Alignment.BottomEnd) {
            val current = remember { mutableIntStateOf(-1) }
            val length = remember { mutableLongStateOf(0L) }
            component.imageView()(
                modifier = Modifier,
                spec = ImageView.Spec(
                    url = avatar,
                    ratio = aspectRatio,
                    contentScale = ContentScale.Crop,
                    blur = 500f,
                )
            )
            component.imageView()(
                modifier = Modifier,
                spec = ImageView.Spec(
                    url = avatar,
                    ratio = aspectRatio
                )
            )
            component.audioPlayer().Thumbnail(
                path = path,
                hasControls = false,
                position = position,
                enable = isActive,
                isActive = isActive,
                length = length,
                current = current,
                modifier = Modifier.padding(horizontal = 24.dp)
                    .padding(vertical = 12.dp)
            )
        }
    }
}
