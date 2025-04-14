package eu.peernetwork.media.core.provider

import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.MediaSelector
import eu.peernetwork.media.core.renderer.VideoPlayer
import eu.peernetwork.media.core.renderer.VideoThumbnail

interface RendererProvider {
    fun mediaSelector(): MediaSelector

    fun imageView(): ImageView

    fun videoThumbnail(): VideoThumbnail

    fun videoPlayer(): VideoPlayer
}
