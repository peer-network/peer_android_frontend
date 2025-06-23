package eu.peernetwork.app.module.media

import dagger.Module
import dagger.Provides
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoPlayer
import eu.peernetwork.media.core.renderer.VideoThumbnail
import eu.peernetwork.media.ui.renderer.ImageViewDelegate
import eu.peernetwork.media.ui.renderer.VideoPlayerDelegate
import eu.peernetwork.media.ui.renderer.VideoThumbnailDelegate
import javax.inject.Singleton

@Module(includes = [
    UsecaseModule::class,
    InteractorModule::class
])
object MediaModule {
    @Provides
    @Singleton
    fun bindImageView(delegate: ImageViewDelegate): ImageView = delegate

    @Provides
    @Singleton
    fun bindVideoThumbnail(delegate: VideoThumbnailDelegate): VideoThumbnail = delegate

    @Provides
    @Singleton
    fun bindVideoPlayer(delegate: VideoPlayerDelegate): VideoPlayer = delegate
}