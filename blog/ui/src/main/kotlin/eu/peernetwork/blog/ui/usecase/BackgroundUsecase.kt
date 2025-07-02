package eu.peernetwork.blog.ui.usecase

import android.graphics.Bitmap
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import javax.inject.Inject

class BackgroundUsecase @Inject constructor(
    private val interactor: ThumbnailInteractor,
) : ParameterizedSuspendableUseCase<BackgroundUsecase.Parameter, Bitmap?> {
    override suspend fun invoke(param: Parameter): Bitmap? {
        var bitmap = interactor.get(param.url)
        return if (bitmap != null) {
            interactor.invalidate()
            bitmap
        } else {
            val dimen = param.width.toFloat()
            interactor.get(param.url, param.type, Pair(dimen, dimen))?.let { foreground ->
                interactor.merge(
                    param.url,
                    param.type,
                    param.width,
                    param.aspectRatio,
                    interactor.blur(foreground, 100),
                    foreground
                )
            }
        }
    }

    data class Parameter(
        val url: String,
        val type: UiMimeType,
        val width: Int,
        val aspectRatio: Float,
    )
}
