package eu.peernetwork.media.ui.usecase

import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import eu.peernetwork.media.core.usecase.DecodeAndCacheThumbnailUsecase
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.min

class DecodeAndCacheThumbnailUsecaseDelegate @Inject constructor(
    private val interactor: ThumbnailInteractor,
    private val dispatcher: Dispatcher
) : DecodeAndCacheThumbnailUsecase {

    override suspend fun invoke(param: DecodeAndCacheThumbnailUsecase.Params): Unit =
        withContext(dispatcher.io) {
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
