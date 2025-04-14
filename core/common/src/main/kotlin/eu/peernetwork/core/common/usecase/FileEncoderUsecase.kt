package eu.peernetwork.core.common.usecase

import java.io.InputStream

interface FileEncoderUsecase : ParameterizedImmediateUseCase<FileEncoderUsecase.Parameter, String> {
    data class Parameter(
        val type: String,
        val content: InputStream
    )
}
