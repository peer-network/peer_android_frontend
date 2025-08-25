package eu.peernetwork.media.core.usecase

import eu.peernetwork.core.common.usecase.ParameterizedImmediateUseCase
import java.io.InputStream

interface FileEncoderUsecase : ParameterizedImmediateUseCase<FileEncoderUsecase.Parameter, String> {
    data class Parameter(
        val type: String,
        val content: InputStream
    )
}
