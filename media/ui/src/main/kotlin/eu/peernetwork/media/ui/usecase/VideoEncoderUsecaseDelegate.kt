package eu.peernetwork.media.ui.usecase

import android.net.Uri
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import eu.peernetwork.media.core.usecase.VideoEncoderUsecase
import javax.inject.Inject

class VideoEncoderUsecaseDelegate @Inject constructor(
    private val trimUsecase: VideoTrimUsecase,
    private val mediaEncoderUsecase: MediaEncoderUsecase
) : VideoEncoderUsecase {
    override suspend fun invoke(param: VideoEncoderUsecase.Parameter): String? {
        return mediaEncoderUsecase(Uri.fromFile(
            trimUsecase(
                VideoTrimUsecase.Parameter(
                    uri = param.uri,
                    offset = param.offset
                )
            )
        ))
    }
}
