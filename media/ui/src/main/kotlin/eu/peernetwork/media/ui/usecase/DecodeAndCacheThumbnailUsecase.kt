package eu.peernetwork.media.ui.usecase

import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.min
import java.io.InputStream
import eu.peernetwork.media.core.model.UiMimeType

class DecodeAndCacheThumbnailUsecase @Inject constructor(
    private val interactor: ThumbnailInteractor,
    private val dispatcher: Dispatcher
) {
    data class Params(
        val key: String,
        val stream: InputStream,
        val boxPx: Int,
        val mime: UiMimeType
    )

    suspend operator fun invoke(param: Params): Unit = withContext(dispatcher.io) {
        val raw = BitmapFactory.decodeStream(param.stream)

        val scale = min(
            param.boxPx.toFloat() / raw.width,
            param.boxPx.toFloat() / raw.height
        )

        val bmp = if (scale < 1f)
            raw.scale((raw.width * scale).toInt(), (raw.height * scale).toInt(), true)
        else raw

        interactor.save(param.key, bmp)
    }
}
