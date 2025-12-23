package eu.peernetwork.media.ui.usecase

import android.graphics.Bitmap
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject
import eu.peernetwork.media.ui.extension.blur

class BlurUsecase @Inject constructor() : ParameterizedSuspendableUseCase<BlurUsecase.Parameter, Bitmap> {
    override suspend fun invoke(param: Parameter): Bitmap {
        return param.bitmap.blur(param.blur.coerceIn(1, 100))
    }

    data class Parameter(
        val bitmap: Bitmap,
        val blur: Int,
    )
}