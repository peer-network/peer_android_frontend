package eu.peernetwork.media.core.provider

import eu.peernetwork.media.core.renderer.AudioPlayer
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.MediaController
import eu.peernetwork.media.core.renderer.VideoPlayer
import eu.peernetwork.media.core.renderer.VideoThumbnail

interface RendererProvider {
    fun imageView(): ImageView

    fun videoThumbnail(): VideoThumbnail

    fun audioPlayer(): AudioPlayer

    fun videoPlayer(): VideoPlayer

    fun mediaController(): MediaController
}
