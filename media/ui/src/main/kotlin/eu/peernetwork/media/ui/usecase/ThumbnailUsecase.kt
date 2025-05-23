package eu.peernetwork.media.ui.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.provider.MediaStore
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.model.UiMimeType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ThumbnailUsecase @Inject constructor(
    private val dispatcher: Dispatcher
) : ParameterizedSuspendableUseCase<ThumbnailUsecase.Parameter, Bitmap?> {
    override suspend fun invoke(param: Parameter): Bitmap? = withContext(dispatcher.io) {
        if (param.type == UiMimeType.Video) {
            ThumbnailUtils.createVideoThumbnail(
                param.thumbnail,
                MediaStore.Images.Thumbnails.MINI_KIND
            )
        } else {
            ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(param.thumbnail),
                250,
                250
            )
        }
    }

    data class Parameter(val thumbnail: String, val type: UiMimeType)
}
