package eu.peernetwork.app.module.core

import dagger.Module
import dagger.Provides
import eu.peernetwork.media.core.provider.VideoProvider
import eu.peernetwork.media.core.usecase.MetaDataUsecase
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.VideoPlayer
import eu.peernetwork.media.core.renderer.VideoThumbnail
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import eu.peernetwork.media.ui.provider.VideoProviderDelegate
import eu.peernetwork.media.ui.usecase.MetaDataUsecaseDelegate
import eu.peernetwork.media.ui.renderer.ImageViewDelegate
import eu.peernetwork.media.ui.renderer.VideoPlayerDelegate
import eu.peernetwork.media.ui.renderer.VideoThumbnailDelegate
import eu.peernetwork.media.ui.usecase.MediaEncoderUsecaseDelegate
import javax.inject.Singleton

@Module
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

    @Provides
    @Singleton
    fun bindVideoProvider(delegate: VideoProviderDelegate): VideoProvider = delegate

    @Provides
    fun bindMediaEncoderUsecase(delegate: MediaEncoderUsecaseDelegate): MediaEncoderUsecase = delegate

    @Provides
    @Singleton
    fun bindMetaDataUsecase(delegate: MetaDataUsecaseDelegate): MetaDataUsecase = delegate
}
