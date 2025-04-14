package eu.peernetwork.app.module.core

import dagger.Binds
import dagger.Module
import eu.peernetwork.media.core.provider.VideoProvider
import eu.peernetwork.media.core.usecase.MetaDataUsecase
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.media.core.renderer.MediaSelector
import eu.peernetwork.media.core.renderer.VideoPlayer
import eu.peernetwork.media.core.renderer.VideoThumbnail
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import eu.peernetwork.media.ui.provider.VideoProviderDelegate
import eu.peernetwork.media.ui.usecase.MetaDataUsecaseDelegate
import eu.peernetwork.media.ui.renderer.ImageViewDelegate
import eu.peernetwork.media.ui.renderer.MediaSelectorDelegate
import eu.peernetwork.media.ui.renderer.VideoPlayerDelegate
import eu.peernetwork.media.ui.renderer.VideoThumbnailDelegate
import eu.peernetwork.media.ui.usecase.MediaEncoderUsecaseDelegate
import javax.inject.Singleton

@Module
interface MediaModule {
    @Binds
    @Singleton
    fun bindMediaSelector(delegate: MediaSelectorDelegate): MediaSelector

    @Binds
    @Singleton
    fun bindImageView(delegate: ImageViewDelegate): ImageView

    @Binds
    @Singleton
    fun bindVideoThumbnail(delegate: VideoThumbnailDelegate): VideoThumbnail

    @Binds
    @Singleton
    fun bindVideoPlayer(delegate: VideoPlayerDelegate): VideoPlayer

    @Binds
    @Singleton
    fun bindVideoProvider(delegate: VideoProviderDelegate): VideoProvider

    @Binds
    fun bindMediaEncoderUsecase(delegate: MediaEncoderUsecaseDelegate): MediaEncoderUsecase

    @Binds
    @Singleton
    fun bindMetaDataUsecase(delegate: MetaDataUsecaseDelegate): MetaDataUsecase
}
