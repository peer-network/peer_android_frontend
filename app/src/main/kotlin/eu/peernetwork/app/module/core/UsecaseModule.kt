package eu.peernetwork.app.module.core

import dagger.Binds
import dagger.Module
import eu.peernetwork.app.usecase.FileEncoderUsecaseDelegate
import eu.peernetwork.app.usecase.TextEncoderUsecaseDelegate
import eu.peernetwork.core.common.usecase.FileEncoderUsecase
import eu.peernetwork.core.common.usecase.TextEncoderUsecase

@Module
internal interface UsecaseModule {
    @Binds
    fun provideTextEncoderUsecase(delegate: TextEncoderUsecaseDelegate): TextEncoderUsecase

    @Binds
    fun provideFileEncoderUsecase(delegate: FileEncoderUsecaseDelegate): FileEncoderUsecase
}
