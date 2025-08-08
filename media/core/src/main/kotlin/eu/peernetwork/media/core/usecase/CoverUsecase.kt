package eu.peernetwork.media.core.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMediaProperty
import eu.peernetwork.media.core.model.UiMimeType
import javax.inject.Inject

class CoverUsecase @Inject constructor(
    private val interactor: ThumbnailInteractor,
) : ParameterizedSuspendableUseCase<CoverUsecase.Parameter, UiMediaProperty?> {
    override suspend fun invoke(param: Parameter): UiMediaProperty? {
        val metadata = interactor.get(
            url = param.url,
            type = param.type,
            frame = param.frame
        )
        return metadata?.bitmap?.let {
            val blur = interactor.blur(it, 10)
            metadata.copy(bitmap = interactor.merge(
                url = "${param.url}?blur=true",
                aspectRatio = it.height / it.width.toFloat(),
                background = blur,
                foreground = it,
                width = param.width,
                height = param.height
            ))
        }
    }

    data class Parameter(
        val url: String,
        val type: UiMimeType,
        val width: Int,
        val height: Int,
        val frame: Long = 0,
    )
}