package eu.peernetwork.app.module.media

import dagger.Module
import dagger.Provides
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import eu.peernetwork.media.core.usecase.MetaDataUsecase
import eu.peernetwork.media.ui.usecase.MediaEncoderUsecaseDelegate
import eu.peernetwork.media.ui.usecase.MetaDataUsecaseDelegate

import javax.inject.Singleton

@Module
object UsecaseModule {
    @Provides
    fun bindMediaEncoderUsecase(delegate: MediaEncoderUsecaseDelegate): MediaEncoderUsecase =
        delegate

    @Provides
    @Singleton
    fun bindMetaDataUsecase(delegate: MetaDataUsecaseDelegate): MetaDataUsecase = delegate

}
