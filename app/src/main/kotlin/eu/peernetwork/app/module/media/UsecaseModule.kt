package eu.peernetwork.app.module.media

import dagger.Module
import dagger.Provides
import eu.peernetwork.media.core.usecase.CroppedPreviewUseCase
import eu.peernetwork.media.core.usecase.DecodeAndCacheThumbnailUsecase
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import eu.peernetwork.media.core.usecase.MetaDataUsecase
import eu.peernetwork.media.core.usecase.VideoTrimUsecase
import eu.peernetwork.media.ui.usecase.CroppedPreviewUsecaseDelegate
import eu.peernetwork.media.ui.usecase.DecodeAndCacheThumbnailUsecaseDelegate
import eu.peernetwork.media.ui.usecase.MediaEncoderUsecaseDelegate
import eu.peernetwork.media.ui.usecase.MetaDataUsecaseDelegate
import eu.peernetwork.media.ui.usecase.VideoTrimUsecaseDelegate
import javax.inject.Singleton

@Module
object UsecaseModule {
    @Provides
    fun bindMediaEncoderUsecase(delegate: MediaEncoderUsecaseDelegate): MediaEncoderUsecase =
        delegate

    @Provides
    @Singleton
    fun bindMetaDataUsecase(delegate: MetaDataUsecaseDelegate): MetaDataUsecase = delegate

    @Provides
    @Singleton
    fun bindVideoTrimUsecase(delegate: VideoTrimUsecaseDelegate): VideoTrimUsecase = delegate

    @Provides
    @Singleton
    fun bindCroppedPreviewUsecase(
        delegate: CroppedPreviewUsecaseDelegate
    ): CroppedPreviewUseCase = delegate

    @Provides
    @Singleton
    fun bindDecodeAndCacheThumbnail(
        delegate: DecodeAndCacheThumbnailUsecaseDelegate
    ): DecodeAndCacheThumbnailUsecase = delegate
}
