package eu.peernetwork.core.common.provider

import eu.peernetwork.core.common.usecase.FileEncoderUsecase
import eu.peernetwork.core.common.usecase.TextEncoderUsecase

interface CoreProvider : Dispatcher.Provider, ServiceProvider {
    fun textEncoderUsecase(): TextEncoderUsecase

    fun fileEncoderUsecase(): FileEncoderUsecase
}
