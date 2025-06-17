package eu.peernetwork.media.core.renderer

import eu.peernetwork.core.ui.renderer.Renderer
import eu.peernetwork.media.core.model.UiMediaProperty

interface ImageView : Renderer.Stateful<ImageView.Spec> {
    data class Spec(
        val url: String,
        val ratio: Float
    )
}
