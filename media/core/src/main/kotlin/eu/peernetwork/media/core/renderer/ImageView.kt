package eu.peernetwork.media.core.renderer

import androidx.compose.ui.layout.ContentScale
import eu.peernetwork.core.ui.renderer.Renderer
import eu.peernetwork.media.core.model.UiMediaProperty

interface ImageView : Renderer.Stateful<ImageView.Spec> {
    data class Spec(
        val url: String,
        val ratio: Float?,
        val contentScale: ContentScale = ContentScale.Fit,
        val blur: Float = 0f,
        val width: Int = 640,
        val zoomable: Boolean = false,
    )
}
