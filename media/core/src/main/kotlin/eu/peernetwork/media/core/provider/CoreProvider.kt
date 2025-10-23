package eu.peernetwork.media.core.provider

import eu.peernetwork.media.core.usecase.FileEncoderUsecase
import eu.peernetwork.media.core.usecase.MetaDataUsecase
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import eu.peernetwork.media.core.usecase.TextEncoderUsecase
import eu.peernetwork.media.core.usecase.TrimUsecase

interface CoreProvider {
    fun videoEncoderUsecase(): TrimUsecase

    fun mediaEncoderUsecase(): MediaEncoderUsecase

    fun metaDataInteractor(): MetaDataUsecase

    fun textEncoderUsecase(): TextEncoderUsecase

    fun fileEncoderUsecase(): FileEncoderUsecase
}
