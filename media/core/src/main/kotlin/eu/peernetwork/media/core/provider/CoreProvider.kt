package eu.peernetwork.media.core.provider

import eu.peernetwork.media.core.usecase.MetaDataUsecase
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase

interface CoreProvider {
    fun mediaEncoderUsecase(): MediaEncoderUsecase

    fun metaDataInteractor(): MetaDataUsecase
}
