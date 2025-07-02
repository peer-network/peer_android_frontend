package eu.peernetwork.media.ui.usecase

import android.graphics.Bitmap
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject
import com.commit451.nativestackblur.NativeStackBlur

class BlurUsecase @Inject constructor() : ParameterizedSuspendableUseCase<BlurUsecase.Parameter, Bitmap> {
    override suspend fun invoke(param: Parameter): Bitmap {
        return NativeStackBlur.process(param.bitmap, param.blur)
    }

    data class Parameter(
        val bitmap: Bitmap,
        val blur: Int,
    )
}
