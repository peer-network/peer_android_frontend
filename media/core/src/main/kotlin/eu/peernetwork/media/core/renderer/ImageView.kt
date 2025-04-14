package eu.peernetwork.media.core.renderer

import eu.peernetwork.media.core.model.Property

interface ImageView : Renderer.Stateful<ImageView.Spec> {
    data class Spec(
        val url: String,
        val property: Property? = null
    )
}
