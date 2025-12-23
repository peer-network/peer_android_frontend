package eu.peernetwork.user.ui.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.core.ui.design.material.DesignZoom
import eu.peernetwork.media.core.renderer.ImageView

@Composable
fun UserModal(
    image: MutableState<String?>,
    component: User.Component
) {
    val isVisible = remember { derivedStateOf { image.value != null } }
    val streamState = remember { derivedStateOf {
        if (image.value == null) {
            DesignStreamState.Default
        } else {
            DesignStreamState.Success(image.value!!)
        }
    } }
    DesignStream(streamState) { avatar ->
        DesignOverlay(
            state = isVisible,
            onDismiss = {
                image.value = null
            }
        ) {
            DesignZoom({
                component.imageView()(
                    Modifier,
                    ImageView.Spec(
                        avatar.value,
                        null,
                        ContentScale.Crop,
                        500f,
                    )
                )
            }) {
                component.imageView()(
                    Modifier,
                    ImageView.Spec(avatar.value, null)
                )
            }
        }
    }
}
