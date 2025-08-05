package eu.peernetwork.media.ui.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.model.UiMetadata
import javax.inject.Inject

class CoverUsecase @Inject constructor(
    private val usecase: MetadataRetrieverUsecase,
    private val interactor: ThumbnailInteractor,
) : ParameterizedSuspendableUseCase<CoverUsecase.Parameter, UiMetadata?> {
    override suspend fun invoke(param: Parameter): UiMetadata? {
        val metadata = usecase(
            MetadataRetrieverUsecase.Parameter(
                url = param.url,
                type = param.type,
                frame = param.frame
            )
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
