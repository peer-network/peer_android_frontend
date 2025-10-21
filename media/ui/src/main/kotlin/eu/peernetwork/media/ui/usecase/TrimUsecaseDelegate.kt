package eu.peernetwork.media.ui.usecase

import eu.peernetwork.media.core.usecase.TrimUsecase
import javax.inject.Inject

class TrimUsecaseDelegate @Inject constructor(
    private val trimUsecase: VideoTrimUsecase
) : TrimUsecase {
    override suspend fun invoke(param: TrimUsecase.Parameter): String {
        return trimUsecase(
            VideoTrimUsecase.Parameter(
                uri = param.uri,
                offset = param.offset
            )
        ).path
    }
}
