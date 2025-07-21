package eu.peernetwork.media.ui.usecase

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject
import androidx.core.graphics.scale

class BitmapMergeUsecase @Inject constructor() : ParameterizedSuspendableUseCase<BitmapMergeUsecase.Parameter, Bitmap> {
    override suspend fun invoke(param: Parameter): Bitmap {
        val ratio = param.foreground.width.toFloat() / param.foreground.height
        val scaleWidth: Int
        val scaleHeight: Int
        if (ratio > param.aspectRatio || param.fit) {
            scaleWidth = param.width
            scaleHeight = (scaleWidth / ratio).toInt()
        } else {
            scaleHeight = param.height
            scaleWidth = (scaleHeight * ratio).toInt()
        }
        val scaledForeground = param.foreground.scale(scaleWidth, scaleHeight)
        val result = param.background.scale(param.width, param.height)
        val overlay = result.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val matrix = ColorMatrix().apply { setScale(0.7f, 0.7f, 0.7f, 1f) }
        canvas.drawBitmap(
            overlay,
            0f,
            0f,
            Paint().apply {colorFilter = ColorMatrixColorFilter(matrix)  }
        )
        val left = ((param.width - scaleWidth) / 2).toFloat()
        val top = ((param.height - scaleHeight) / 2).toFloat()
        canvas.drawBitmap(scaledForeground, left, top, null)
        return result
    }

    data class Parameter(
        val aspectRatio: Float,
        val background: Bitmap,
        val foreground: Bitmap,
        val width: Int,
        val height: Int = (width / aspectRatio).toInt(),
        val fit: Boolean = false
    )
}
