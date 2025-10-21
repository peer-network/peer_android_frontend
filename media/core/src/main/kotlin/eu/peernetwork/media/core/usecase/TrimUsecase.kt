package eu.peernetwork.media.core.usecase

import android.net.Uri
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.model.UiOffset

interface TrimUsecase : ParameterizedSuspendableUseCase<TrimUsecase.Parameter, String> {
    data class Parameter(
        val uri: Uri,
        val offset: UiOffset
    )
}
