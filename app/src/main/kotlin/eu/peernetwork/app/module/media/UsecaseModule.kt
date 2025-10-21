package eu.peernetwork.app.module.media

import dagger.Module
import dagger.Provides
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import eu.peernetwork.media.core.usecase.MetaDataUsecase
import eu.peernetwork.media.core.usecase.TrimUsecase
import eu.peernetwork.media.ui.usecase.MediaEncoderUsecaseDelegate
import eu.peernetwork.media.ui.usecase.MetaDataUsecaseDelegate
import eu.peernetwork.media.ui.usecase.TrimUsecaseDelegate
import javax.inject.Singleton

@Module
object UsecaseModule {
    @Provides
    fun bindVideoEncoderUsecase(delegate: TrimUsecaseDelegate): TrimUsecase = delegate

    @Provides
    fun bindMediaEncoderUsecase(delegate: MediaEncoderUsecaseDelegate): MediaEncoderUsecase = delegate

    @Provides
    @Singleton
    fun bindMetaDataUsecase(delegate: MetaDataUsecaseDelegate): MetaDataUsecase = delegate
}
