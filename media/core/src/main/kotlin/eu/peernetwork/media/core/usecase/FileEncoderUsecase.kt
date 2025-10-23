package eu.peernetwork.media.core.usecase

import eu.peernetwork.core.common.usecase.ParameterizedBlockingUseCase
import java.io.InputStream

interface FileEncoderUsecase : ParameterizedBlockingUseCase<FileEncoderUsecase.Parameter, String> {
    data class Parameter(
        val type: String,
        val content: InputStream
    )
}
