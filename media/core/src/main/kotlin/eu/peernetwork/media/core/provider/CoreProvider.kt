package eu.peernetwork.media.core.provider

import eu.peernetwork.media.core.usecase.MetaDataUsecase
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import eu.peernetwork.media.core.usecase.VideoEncoderUsecase

interface CoreProvider {
    fun videoEncoderUsecase(): VideoEncoderUsecase

    fun mediaEncoderUsecase(): MediaEncoderUsecase

    fun metaDataInteractor(): MetaDataUsecase
}
