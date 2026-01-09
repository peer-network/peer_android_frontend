package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import eu.peernetwork.blog.ui.model.UiPostType
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.blog.ui.post.PostInteractor.Companion.LocalPostInteractor
import eu.peernetwork.core.ui.design.material.DesignThumbnail
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoThumbnail

@Composable
fun PostMedia(
    type: UiPostType,
    path: String,
    cover: String,
    expanded: Boolean,
    isAdmin: Boolean,
    isAccessible: Boolean,
    ratio: Float,
    status: UiStatus,
    enable: State<Boolean>,
    isPlaying: State<Boolean>,
    onClick: (Boolean) -> Unit
) {
    val interactor = LocalPostInteractor.current
    val configuration = LocalConfiguration.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val bitmaps = interactor.observe()
    val thumbnail = remember { derivedStateOf { bitmaps.value[path] } }
    val pause = remember { mutableStateOf(false) }
    val isEnabled = remember { derivedStateOf { !pause.value && enable.value && isPlaying.value } }
    if (type == UiPostType.IMAGE) {
        interactor.component().imageView()(
            modifier = Modifier,
            spec = ImageView.Spec(
                url = path,
                ratio = ratio,
                contentScale = ContentScale.Crop,
                blur = 500f,
            )
        )
        PostMask(
            status = status,
            isAuthor = isAdmin,
            isAccessible = isAccessible,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio)) {
            interactor.component().imageView()(
                modifier = Modifier,
                spec = ImageView.Spec(
                    url = path,
                    ratio = ratio
                )
            )
        }
    } else if (type == UiPostType.VIDEO) {
        DesignThumbnail(
            enable = enable,
            thumbnail = path,
            bitmap = thumbnail,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio)
        ) {
            interactor.background(
                media = path,
                aspectRatio = ratio,
                width = configuration.screenWidthDp,
                height = (configuration.screenWidthDp / ratio).toInt()
            )
        }
        PostMask(
            status = status,
            isAuthor = isAdmin,
            isAccessible = isAccessible,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio)) {
            interactor.component().videoThumbnail()(
                Modifier,
                spec = VideoThumbnail.Spec(
                    url = path,
                    ratio = ratio,
                    enabled = isEnabled,
                    isPlaying = isPlaying
                )
            )
        }
    } else if (type == UiPostType.AUDIO) {
        Box(contentAlignment = Alignment.BottomEnd) {
            val length = remember { mutableLongStateOf(0L) }
            if (expanded) {
                interactor.component().imageView()(
                    modifier = Modifier,
                    spec = ImageView.Spec(
                        url = cover,
                        ratio = ratio,
                        contentScale = ContentScale.Crop,
                        blur = 500f,
                    )
                )
                PostMask(
                    status = status,
                    isAuthor = isAdmin,
                    isAccessible = isAccessible,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio)) {
                    interactor.component().imageView()(
                        modifier = Modifier,
                        spec = ImageView.Spec(
                            url = cover,
                            ratio = ratio
                        )
                    )
                }
            }
            if (isAccessible) {
                interactor.component().mediaController().Content(
                    path = path,
                    expanded = !expanded,
                    enabled = isEnabled,
                    length = length,
                    isPlaying = isPlaying,
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp),
                    onToggle = onClick
                )
            }
        }
    }
    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    pause.value = false
                }
                Lifecycle.Event.ON_PAUSE -> {
                    pause.value = true
                }
                else -> Unit
            }
        }
    }
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
    }
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
