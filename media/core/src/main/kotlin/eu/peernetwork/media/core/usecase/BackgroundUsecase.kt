package eu.peernetwork.media.core.usecase

import android.graphics.Bitmap
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType

class BackgroundUsecase(
    private val interactor: ThumbnailInteractor,
) : ParameterizedSuspendableUseCase<BackgroundUsecase.Parameter, Bitmap?> {
    override suspend fun invoke(param: Parameter): Bitmap? {
        var bitmap = interactor.get(param.url)
        return bitmap
            ?: interactor.get(param.url, param.type)?.let { foreground ->
                interactor.merge(
                    param.url,
                    param.aspectRatio,
                    interactor.blur(foreground, 10),
                    foreground,
                    param.width,
                    param.height,
                    param.fit
                )
            }
    }

    data class Parameter(
        val url: String,
        val type: UiMimeType,
        val width: Int,
        val height: Int,
        val aspectRatio: Float,
        val fit: Boolean = false
    )
}